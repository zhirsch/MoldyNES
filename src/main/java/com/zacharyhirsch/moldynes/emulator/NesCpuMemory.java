package com.zacharyhirsch.moldynes.emulator;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Objects;

// https://www.nesdev.org/wiki/CPU_memory_map
public final class NesCpuMemory {

  private interface Region {

    UInt8 fetchByte(UInt16 address);

    UInt16 fetchWord(UInt16 address);

    void storeByte(UInt16 address, UInt8 value);

    void storeWord(UInt16 address, UInt16 value);
  }

  private final ImmutableRangeMap<UInt16, Region> regions;

  private NesCpuMemory(ImmutableRangeMap<UInt16, Region> regions) {
    this.regions = regions;
  }

  public static final class Builder {

    private final RangeMap<UInt16, Region> regions = TreeRangeMap.create();

    public Builder() {
      ram(0x0000, 0x0800, ByteBuffer.allocateDirect(0x0800).order(ByteOrder.LITTLE_ENDIAN));
      mirror(0x0800, 0x1000, 0x0000);
      mirror(0x1000, 0x1800, 0x0000);
      mirror(0x1800, 0x2000, 0x0000);
      // 0x2000 - 0x2007    = NES PPU registers
      for (int off = 0x2008; off < 0x4000; off += 0x08) {
        mirror(off, off + 0x08, 0x2000);
      }
      // 0x4000 - 0x4017    = NES APU and I/O registers
      // 0x4018 - 0x401f    = APU and I/O functionality that is normally disabled
      // 0x4020 - 0xffff    = Cartridge space
    }

    public Builder ram(int offset, int len, ByteBuffer buffer) {
      Range<UInt16> range = Range.closedOpen(UInt16.cast(offset), UInt16.cast(offset + len));
      regions.put(range, new RamRegion(buffer.order(ByteOrder.LITTLE_ENDIAN)));
      return this;
    }

    public Builder rom(int offset, int len, ByteBuffer buffer) {
      Range<UInt16> range = Range.closedOpen(UInt16.cast(offset), UInt16.cast(offset + len));
      regions.put(range, new RomRegion(buffer.order(ByteOrder.LITTLE_ENDIAN)));
      return this;
    }

    public Builder mirror(int offset, int len, int dst) {
      Range<UInt16> range = Range.closedOpen(UInt16.cast(offset), UInt16.cast(offset + len));
      regions.put(range, new MirrorRegion(regions, UInt16.cast(dst)));
      return this;
    }

    public NesCpuMemory build() {
      return new NesCpuMemory(ImmutableRangeMap.copyOf(regions));
    }
  }

  public UInt8 fetchByte(UInt16 address) {
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(address));
    return entry.getValue().fetchByte(address.sub(entry.getKey().lowerEndpoint()));
  }

  public UInt8 fetchByte(UInt16 address, UInt8 index) {
    UInt16 addr = address.add(index);
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetchByte(addr.sub(entry.getKey().lowerEndpoint()));
  }

  public UInt16 fetchWord(UInt16 address) {
    // TODO: check for same page
    UInt8 lsb = fetchByte(address);
    UInt8 msb = fetchByte(address.add(UInt8.cast(1)));
    return new UInt16(lsb, msb);
  }

  public UInt8 fetchZeropageByte(UInt8 zeropage) {
    UInt16 addr = UInt16.cast(zeropage);
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetchByte(addr.sub(entry.getKey().lowerEndpoint()));
  }

  public UInt8 fetchZeropageByte(UInt8 zeropage, UInt8 index) {
    UInt16 addr = UInt16.cast(NesAlu.add(zeropage, index, false).output());
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetchByte(addr.sub(entry.getKey().lowerEndpoint()));
  }

  public UInt16 fetchZeropageWord(UInt8 zeropage) {
    // TODO: check for same page
    UInt8 lsb = fetchZeropageByte(zeropage);
    UInt8 rhs = UInt8.cast(1);
    UInt8 msb = fetchZeropageByte(NesAlu.add(zeropage, rhs, false).output());
    return new UInt16(lsb, msb);
  }

  public UInt16 fetchZeropageWord(UInt8 zeropage, UInt8 index) {
    UInt8 address = NesAlu.add(zeropage, index, false).output();
    // TODO: check for same page
    UInt8 lsb = fetchZeropageByte(address);
    UInt8 rhs = UInt8.cast(1);
    UInt8 msb = fetchZeropageByte(NesAlu.add(address, rhs, false).output());
    return new UInt16(lsb, msb);
  }

  public void storeByte(UInt16 address, UInt8 value) {
    storeByte(regions, address, value);
  }

  public void storeByte(UInt16 address, UInt8 index, UInt8 value) {
    storeByte(regions, address.add(index), value);
  }

  public void storeWord(UInt16 address, UInt16 value) {
    storeWord(regions, address, value);
  }

  public void storeZeropageByte(UInt8 zeropage, UInt8 value) {
    storeByte(regions, UInt16.cast(zeropage), value);
  }

  public void storeZeropageByte(UInt8 zeropage, UInt8 index, UInt8 value) {
    storeByte(regions, UInt16.cast(NesAlu.add(zeropage, index, false).output()), value);
  }

  private static UInt8 fetchByte(RangeMap<UInt16, Region> regions, UInt16 addr) {
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetchByte(addr.sub(entry.getKey().lowerEndpoint()));
  }

  private static UInt16 fetchWord(RangeMap<UInt16, Region> regions, UInt16 addr) {
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    return entry.getValue().fetchWord(addr.sub(entry.getKey().lowerEndpoint()));
  }

  private static void storeByte(RangeMap<UInt16, Region> regions, UInt16 addr, UInt8 value) {
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    entry.getValue().storeByte(addr.sub(entry.getKey().lowerEndpoint()), value);
  }

  private static void storeWord(RangeMap<UInt16, Region> regions, UInt16 addr, UInt16 value) {
    Map.Entry<Range<UInt16>, Region> entry = Objects.requireNonNull(regions.getEntry(addr));
    entry.getValue().storeWord(addr.sub(entry.getKey().lowerEndpoint()), value);
  }

  private record RamRegion(ByteBuffer ram) implements Region {

    @Override
    public UInt8 fetchByte(UInt16 address) {
      return UInt8.cast(ram.get(Short.toUnsignedInt(address.value())));
    }

    @Override
    public UInt16 fetchWord(UInt16 address) {
      return UInt16.cast(ram.getShort(Short.toUnsignedInt(address.value())));
    }

    @Override
    public void storeByte(UInt16 address, UInt8 value) {
      ram.put(Short.toUnsignedInt(address.value()), value.value());
    }

    @Override
    public void storeWord(UInt16 address, UInt16 value) {
      ram.putShort(Short.toUnsignedInt(address.value()), value.value());
    }
  }

  private record RomRegion(ByteBuffer rom) implements Region {

    @Override
    public UInt8 fetchByte(UInt16 address) {
      return UInt8.cast(rom.get(Short.toUnsignedInt(address.value())));
    }

    @Override
    public UInt16 fetchWord(UInt16 address) {
      return UInt16.cast(rom.getShort(Short.toUnsignedInt(address.value())));
    }

    @Override
    public void storeByte(UInt16 address, UInt8 value) {
      rom.put(Short.toUnsignedInt(address.value()), value.value());
    }

    @Override
    public void storeWord(UInt16 address, UInt16 value) {
      rom.putShort(Short.toUnsignedInt(address.value()), value.value());
    }
  }

  private record MirrorRegion(RangeMap<UInt16, Region> regions, UInt16 dst) implements Region {

    @Override
    public UInt8 fetchByte(UInt16 address) {
      return NesCpuMemory.fetchByte(regions, dst.add(address));
    }

    @Override
    public UInt16 fetchWord(UInt16 address) {
      return NesCpuMemory.fetchWord(regions, dst.add(address));
    }

    @Override
    public void storeByte(UInt16 address, UInt8 value) {
      NesCpuMemory.storeByte(regions, dst.add(address), value);
    }

    @Override
    public void storeWord(UInt16 address, UInt16 value) {
      NesCpuMemory.storeWord(regions, dst.add(address), value);
    }
  }
}