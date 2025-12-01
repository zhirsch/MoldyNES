package com.zacharyhirsch.moldynes.emulator.cpu;

final class NesCpuNmiPin {

  private boolean value = false;
  private boolean pending = false;

  boolean value() {
    return value;
  }

  void set(boolean v) {
    value = value || pending;
    pending = v;
  }

  void reset() {
    value = false;
    pending = false;
  }
}
