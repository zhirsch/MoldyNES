package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuPatternByte {

  private byte value = 0;
  private byte pending = 0;

  byte value(int i) {
    return (byte) ((value >>> (7 - i)) & 1);
  }

  void set(byte v) {
    pending = v;
  }

  void shift() {
    value <<= 1;
  }

  void reload() {
    value = pending;
  }
}
