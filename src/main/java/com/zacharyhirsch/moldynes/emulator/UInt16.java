package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;

public record UInt16(short value) {

  public UInt16(UInt8 msb, UInt8 lsb) {
    this((short) ((toUnsignedInt(msb.value()) << 8) | toUnsignedInt(lsb.value())));
  }

  public static UInt16 cast(int value) {
    return new UInt16((short) value);
  }

  @Override
  public String toString() {
    return String.format("%04x", value());
  }

  public UInt8 lsb() {
    return UInt8.cast(value());
  }

  public UInt8 msb() {
    return UInt8.cast(value() >>> 8);
  }
}
