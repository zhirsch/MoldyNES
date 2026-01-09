package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuAttribute {

  private final BitPlane<Byte> latch;
  private final BitPlane<Byte> shift;

  private byte value;

  NesPpuAttribute() {
    this.latch = new BitPlane<>((byte) 0, (byte) 0);
    this.shift = new BitPlane<>((byte) 0, (byte) 0);
    this.value = 0;
  }

  BitPlane<Byte> value() {
    return shift;
  }

  void set(byte value) {
    this.value = value;
  }

  void shift() {
    shift.lo = (byte) ((shift.lo << 1) | (latch.lo));
    shift.hi = (byte) ((shift.hi << 1) | (latch.hi));
  }

  void reload() {
    latch.lo = (byte) ((value & 0b0000_0001) >>> 0);
    latch.hi = (byte) ((value & 0b0000_0010) >>> 1);
  }
}
