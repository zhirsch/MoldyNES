package com.zacharyhirsch.moldynes.emulator;

public record UInt16(short value) implements Comparable<UInt16> {

  public UInt16(UInt8 lsb, UInt8 msb) {
    this(ByteUtil.compose(lsb.value(), msb.value()));
  }

  public static UInt16 cast(int value) {
    return new UInt16((short) value);
  }

  public static UInt16 cast(UInt8 lsb) {
    return new UInt16(lsb, UInt8.cast(0x00));
  }

  @Override
  public String toString() {
    return String.format("%04x", value());
  }

  @Override
  public int compareTo(UInt16 o) {
    return Short.compareUnsigned(value, o.value);
  }

  public UInt8 lsb() {
    return UInt8.cast(value());
  }

  public UInt8 msb() {
    return UInt8.cast(value() >>> 8);
  }
}
