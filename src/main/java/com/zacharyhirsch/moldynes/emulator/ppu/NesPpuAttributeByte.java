package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuAttributeByte {

  private byte value = 0;
  private byte pending = 0;

  byte value() {
    return (byte) (value & 0b0000_0011);
  }

  void set(byte v) {
    pending = v;
  }

  void shift() {}

  void reload() {
    value = pending;
  }
}
