package com.zacharyhirsch.moldynes.emulator.memory;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.zacharyhirsch.moldynes.emulator.NesDevice;
import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.nio.ByteBuffer;
import java.util.Objects;

public class NesMemory {

  private final ImmutableRangeMap<Integer, Region> regions;
  private final NesPpu ppu;

  private NesMemory(ImmutableRangeMap<Integer, Region> regions, NesPpu ppu) {
    this.regions = regions;
    this.ppu = ppu;
  }

  private record Region(short base, NesDevice device) {}

  public static final class Builder {

    private final RangeMap<Integer, Region> regions = TreeRangeMap.create();
    private final NesPpu ppu;

    public Builder(NesPpu ppu, NesApu apu) {
      this.ppu = ppu;

      // Internal memory
      NesRam ram = new NesRam(ByteBuffer.allocateDirect(0x0800));
      for (int off = 0x0000; off < 0x2000; off += 0x0800) {
        put(off, 0x0800, ram);
      }

      // PPU registers
      for (int off = 0x2000; off < 0x4000; off += 0x0008) {
        put(off, 0x0008, ppu);
      }
      // PPU OAM DMA
      put(0x4014, 0x0001, ppu);

      // APU registers
      put(0x4000, 0x0004, apu); // pulse 1
      put(0x4004, 0x0004, apu); // pulse 2
      put(0x4008, 0x0004, apu); // triangle
      put(0x400c, 0x0004, apu); // noise
      put(0x4010, 0x0004, apu); // dmc
      put(0x4015, 0x0001, apu); // status
      put(0x4017, 0x0001, apu); // counter

      // Input devices
      put(0x4016, 0x0001, new NesRam(ByteBuffer.allocateDirect(1)));
    }

    public Builder put(int offset, int len, NesDevice device) {
      Range<Integer> range = Range.closedOpen(offset, offset + len);
      regions.put(range, new Region((short) offset, device));
      return this;
    }

    public NesMemory build() {
      return new NesMemory(ImmutableRangeMap.copyOf(regions), ppu);
    }
  }

  public byte fetch(byte adh, byte adl) {
    short address = (short) ((adh << 8) | Byte.toUnsignedInt(adl));
    Region region = getRegion(address);
    return region.device.fetch((short) (address - region.base));
  }

  public byte fetchDebug(byte adh, byte adl) {
    short address = (short) ((adh << 8) | Byte.toUnsignedInt(adl));
    Region region = getRegion(address);
    if (!(region.device instanceof NesRam || region.device instanceof NesRom)) {
      return (byte) 0xff;
    }
    return region.device.fetch((short) (address - region.base));
  }

  public void store(byte adh, byte adl, byte data) {
    short address = (short) ((adh << 8) | Byte.toUnsignedInt(adl));
    if (address == 0x4014) {
      byte[] buffer = new byte[0x100];
      for (int i = 0; i < 256; i++) {
        buffer[i] = fetch(data, (byte) i);
      }
      ppu.writeOamDma(buffer);
      return;
    }
    Region region = getRegion(address);
    region.device.store((short) (address - region.base), data);
  }

  private Region getRegion(short address) {
    Region region = regions.get(Short.toUnsignedInt(address));
    return Objects.requireNonNull(region, () -> "%04x".formatted(address));
  }
}
