package com.zacharyhirsch.moldynes.emulator;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import java.nio.ByteBuffer;
import java.util.Objects;

// https://www.nesdev.org/wiki/CPU_memory_map
public final class NesCpuMemory {

  private final ImmutableRangeMap<Integer, Region> regions;

  private NesCpuMemory(ImmutableRangeMap<Integer, Region> regions) {
    this.regions = regions;
  }

  public static final class Builder {

    private final RangeMap<Integer, Region> regions = TreeRangeMap.create();

    public Builder() {
      // Internal memory
      ByteBuffer internalMemory = ByteBuffer.allocateDirect(0x0800);
      for (int off = 0x0000; off < 0x2000; off += 0x0800) {
        bytes(off, 0x0800, internalMemory);
      }

      // PPU registers
      NesPpu ppu = new NesPpu();
      for (int off = 0x2000; off < 0x4000; off += 0x0008) {
        registers(off, 0x0008, ppu);
      }

      // APU registers
      registers(0x4000, 0x0018, new NesApu());
    }

    public Builder bytes(int offset, int len, ByteBuffer buffer) {
      Range<Integer> range = Range.closed(offset, offset + len - 1);
      regions.put(range, new BytesRegion(UInt16.cast(offset), buffer));
      return this;
    }

    public Builder registers(int offset, int len, NesDevice device) {
      Range<Integer> range = Range.closed(offset, offset + len - 1);
      regions.put(range, new RegistersRegion(device));
      return this;
    }

    public NesCpuMemory build() {
      return new NesCpuMemory(ImmutableRangeMap.copyOf(regions));
    }
  }

  public UInt8 fetch(UInt16 address) {
    return getRegion(address).fetch(address);
  }

  public void store(UInt16 address, UInt8 value) {
    getRegion(address).store(address, value);
  }

  private Region getRegion(UInt16 address) {
    int key = Short.toUnsignedInt(address.value());
    return Objects.requireNonNull(regions.get(key), address::toString);
  }

  private interface Region {

    UInt8 fetch(UInt16 address);

    void store(UInt16 address, UInt8 value);
  }

  private record BytesRegion(UInt16 base, ByteBuffer ram) implements Region {

    @Override
    public UInt8 fetch(UInt16 address) {
      int index = Short.toUnsignedInt(address.value()) - Short.toUnsignedInt(base.value());
      return UInt8.cast(ram.get(index));
    }

    @Override
    public void store(UInt16 address, UInt8 value) {
      int index = Short.toUnsignedInt(address.value()) - Short.toUnsignedInt(base.value());
      ram.put(index, value.value());
    }
  }

  private record RegistersRegion(NesDevice device) implements Region {

    @Override
    public UInt8 fetch(UInt16 address) {
      return device.readRegister(address);
    }

    @Override
    public void store(UInt16 address, UInt8 value) {
      device.writeRegister(address, value);
    }
  }
}
