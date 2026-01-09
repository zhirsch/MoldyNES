package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuLinearCounter {

  private int period;
  private boolean control;
  private int value;
  private boolean reload;

  NesApuLinearCounter() {
    this.period = 0;
    this.control = false;
    this.value = 0;
    this.reload = false;
  }

  void tick() {
    if (reload) {
      value = period;
    } else if (value > 0) {
      value--;
    }
    if (!control) {
      reload = false;
    }
  }

  int value() {
    return value;
  }

  void setPeriod(int period) {
    this.period = period;
  }

  void setControl(boolean control) {
    this.control = control;
  }

  void setReload(boolean reload) {
    this.reload = reload;
  }
}
