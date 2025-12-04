package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuAttributeByte {

  private byte value = 0;
  private byte pending = 0;
  private byte pending2 = 0;

  byte value() {
    return (byte) (value & 0b0000_0011);
  }

  void set(byte v) {
    pending = v;
  }

  void shift() {}

  void reload() {
    // Perhaps this should be a latch or something? Need to delay two cycles
    // so that the reads at the end of the previous scanline are queued up
    // for the current scanline.
    value = pending2;
    pending2 = pending;
  }
}
