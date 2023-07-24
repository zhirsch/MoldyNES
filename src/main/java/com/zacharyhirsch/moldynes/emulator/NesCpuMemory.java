package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;
import static java.lang.Short.toUnsignedInt;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.zacharyhirsch.moldynes.emulator.memory.Index;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Objects;

// https://www.nesdev.org/wiki/CPU_memory_map
public class NesCpuMemory {

  private interface Region {

    <T extends Number> T fetch(int address, Class<T> clazz);

    <T extends Number> void store(int address, T value);
  }

  private final ImmutableRangeMap<Integer, Region> regions;

  private NesCpuMemory(ImmutableRangeMap<Integer, Region> regions) {
    this.regions = regions;
  }

  public static final class Builder {

    private final RangeMap<Integer, Region> regions;

    public Builder() {
      ByteBuffer ram = ByteBuffer.allocateDirect(0x0800).order(ByteOrder.LITTLE_ENDIAN);

      regions = TreeRangeMap.create();
      regions.put(Range.closed(0x0000, 0x07ff), new RamRegion(ram));
      regions.put(Range.closed(0x0800, 0x0fff), new MirrorRegion(regions, 0x0000));
      regions.put(Range.closed(0x1000, 0x17ff), new MirrorRegion(regions, 0x0000));
      regions.put(Range.closed(0x1800, 0x1fff), new MirrorRegion(regions, 0x0000));
      // 0x2000 - 0x2007    = NES PPU registers
      for (int off = 0x2008; off < 0x4000; off += 0x8) {
        regions.put(Range.closedOpen(off, off + 0x08), new MirrorRegion(regions, 0x2000));
      }
      // 0x4000 - 0x4017    = NES APU and I/O registers
      // 0x4018 - 0x401f    = APU and I/O functionality that is normally disabled
      // 0x4020 - 0xffff    = Cartridge space
    }

    public Builder load(int offset, int len, ByteBuffer buffer) {
      Range<Integer> range = Range.closedOpen(offset, offset + len);
      regions.put(range, new RomRegion(buffer.order(ByteOrder.LITTLE_ENDIAN)));
      return this;
    }

    public Builder mirror(int offset, int len, int dst) {
      Range<Integer> range = Range.closedOpen(offset, offset + len);
      regions.put(range, new MirrorRegion(regions, dst));
      return this;
    }

    public NesCpuMemory build() {
      return new NesCpuMemory(ImmutableRangeMap.copyOf(regions));
    }
  }

  public <T extends Number> T fetch(short address, Class<T> clazz) {
    return fetch(regions, toUnsignedInt(address), clazz);
  }

  public <T extends Number> T fetch(short address, Index index, Class<T> clazz) {
    short addr = (short) (toUnsignedInt(address) + toUnsignedInt(index.get()));
    return fetch(regions, addr, clazz);
  }

  public <T extends Number> T fetchZeropage(byte zeropage, Class<T> clazz) {
    return fetch(regions, toUnsignedInt(zeropage) & 0x00ff, clazz);
  }

  public <T extends Number> T fetchZeropage(byte zeropage, Index index, Class<T> clazz) {
    return fetch(regions, (toUnsignedInt(zeropage) + toUnsignedInt(index.get())) & 0x00ff, clazz);
  }

  public <T extends Number> void store(short address, T value) {
    store(regions, toUnsignedInt(address), value);
  }

  public <T extends Number> void store(short address, Index index, T value) {
    store(regions, toUnsignedInt(address) + toUnsignedInt(index.get()), value);
  }

  public <T extends Number> void storeZeropage(byte zeropage, T value) {
    store(regions, toUnsignedInt(zeropage) & 0x00ff, value);
  }

  public <T extends Number> void storeZeropage(byte zeropage, Index index, T value) {
    store(regions, (toUnsignedInt(zeropage) + toUnsignedInt(index.get())) & 0x00ff, value);
  }

  private static <T extends Number> T fetch(
      RangeMap<Integer, Region> regions, int addr, Class<T> clazz) {
    Map.Entry<Range<Integer>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetch(addr - entry.getKey().lowerEndpoint(), clazz);
  }

  private static <T extends Number> void store(
      RangeMap<Integer, Region> regions, int addr, T value) {
    if (addr == 0x02) {
      System.out.printf("0x02  = %02x", value.byteValue());
    }
    if (addr == 0x03) {
      System.out.printf("0x03  = %02x", value.byteValue());
    }
    Map.Entry<Range<Integer>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    entry.getValue().store(addr - entry.getKey().lowerEndpoint(), value);
  }

  private record RamRegion(ByteBuffer ram) implements Region {

    @Override
    public <T extends Number> T fetch(int address, Class<T> clazz) {
      if (clazz.equals(Byte.class)) {
        return clazz.cast(ram.get(address));
      }
      if (clazz.equals(Short.class)) {
        return clazz.cast(ram.getShort(address));
      }
      throw new IllegalArgumentException(clazz.toString());
    }

    @Override
    public <T extends Number> void store(int address, T value) {
      if (value.getClass().equals(Byte.class)) {
        ram.put(address, value.byteValue());
        return;
      }
      if (value.getClass().equals(Short.class)) {
        ram.putShort(address, value.shortValue());
        return;
      }
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

  private record RomRegion(ByteBuffer rom) implements Region {

    @Override
    public <T extends Number> T fetch(int address, Class<T> clazz) {
      if (clazz.equals(Byte.class)) {
        return clazz.cast(rom.get(address));
      }
      if (clazz.equals(Short.class)) {
        return clazz.cast(rom.getShort(address));
      }
      throw new IllegalArgumentException(clazz.toString());
    }

    @Override
    public <T extends Number> void store(int address, T value) {
      if (value.getClass().equals(Byte.class)) {
        rom.put(address, value.byteValue());
        return;
      }
      if (value.getClass().equals(Short.class)) {
        rom.putShort(address, value.shortValue());
        return;
      }
      throw new IllegalArgumentException(value.getClass().toString());
    }
  }

  private record MirrorRegion(RangeMap<Integer, Region> regions, int dst) implements Region {

    @Override
    public <T extends Number> T fetch(int address, Class<T> clazz) {
      return NesCpuMemory.fetch(regions, dst + address, clazz);
    }

    @Override
    public <T extends Number> void store(int address, T value) {
      NesCpuMemory.store(regions, dst + address, value);
    }
  }
}
