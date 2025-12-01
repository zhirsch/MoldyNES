package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuNametableByte {

  private byte value = 0;
  private byte pending = 0;

  byte value() {
    return value;
  }

  void set(byte v) {
    pending = v;
  }

  void shift() {}

  void reload() {
    value = pending;
  }
}
