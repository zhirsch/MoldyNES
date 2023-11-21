package com.zacharyhirsch.moldynes.emulator;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import java.nio.ByteBuffer;
import java.util.Objects;

public class NesCpuMemoryMap {

  private final ImmutableRangeMap<Integer, Region> regions;

  private NesCpuMemoryMap(ImmutableRangeMap<Integer, Region> regions) {
    this.regions = regions;
  }

  private record Region(short base, NesDevice device) {}

  public static final class Builder {

    private final RangeMap<Integer, Region> regions = TreeRangeMap.create();

    public Builder() {
      // Internal memory
      NesRam ram = new NesRam(ByteBuffer.allocateDirect(0x0800));
      for (int off = 0x0000; off < 0x2000; off += 0x0800) {
        put(off, 0x0800, ram);
      }

      // PPU registers
      NesPpu ppu = new NesPpu();
      for (int off = 0x2000; off < 0x4000; off += 0x0008) {
        put(off, 0x0008, ppu);
      }

      // APU registers
      put(0x4000, 0x0018, new NesApu());
    }

    public Builder put(int offset, int len, NesDevice device) {
      Range<Integer> range = Range.closedOpen(offset, offset + len);
      regions.put(range, new Region((short) offset, device));
      return this;
    }

    public NesCpuMemoryMap build() {
      return new NesCpuMemoryMap(ImmutableRangeMap.copyOf(regions));
    }
  }

  public byte fetch(byte adh, byte adl) {
    short address = (short) ((adh << 8) | adl);
    Region region = getRegion(address);
    return region.device.fetch((short) (address - region.base));
  }

  public void store(byte adh, byte adl, byte data) {
    short address = (short) ((adh << 8) | adl);
    Region region = getRegion(address);
    region.device.store((short) (address - region.base), data);
  }

  private Region getRegion(short address) {
    Region region = regions.get(Short.toUnsignedInt(address));
    return Objects.requireNonNull(region, () -> "%04x".formatted(address));
  }
}
