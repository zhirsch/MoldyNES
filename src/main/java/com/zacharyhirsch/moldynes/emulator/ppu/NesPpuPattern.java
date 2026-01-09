package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuPattern {

  private final BitPlane<Byte> latch;
  private final BitPlane<Short> shift;

  NesPpuPattern() {
    this.latch = new BitPlane<>((byte) 0, (byte) 0);
    this.shift = new BitPlane<>((short) 0, (short) 0);
  }

  BitPlane<Byte> value() {
    return new BitPlane<>((byte) (shift.lo >>> 8), (byte) (shift.hi >>> 8));
  }

  void setLo(byte value) {
    latch.lo = value;
  }

  void setHi(byte value) {
    latch.hi = value;
  }

  void shift() {
    shift.lo = (short) ((shift.lo << 1) | 0);
    shift.hi = (short) ((shift.hi << 1) | 1);
  }

  void reload() {
    shift.lo = (short) ((shift.lo & 0b1111_1111_0000_0000) | (latch.lo & 0b0000_0000_1111_1111));
    shift.hi = (short) ((shift.hi & 0b1111_1111_0000_0000) | (latch.hi & 0b0000_0000_1111_1111));
  }
}
