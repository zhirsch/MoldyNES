package com.zacharyhirsch.moldynes.emulator;

public record UInt8(byte value) {

  public static UInt8 cast(int value) {
    return new UInt8((byte) value);
  }

  @Override
  public String toString() {
    return String.format("%02x", value());
  }

  public boolean isZero() {
    return value() == 0;
  }

  public int bit(int i) {
    return (Byte.toUnsignedInt(value()) >> i) & 1;
  }

  public UInt8 signum() {
    return UInt8.cast(value >= 0 ? 1 : -1);
  }
}
