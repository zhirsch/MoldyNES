package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuTimer {

  private int rate;
  private int value;

  NesApuTimer() {
    this.rate = 0;
    this.value = 0;
  }

  boolean tick() {
    if (rate == 0) {
      return false;
    }
    value = Math.clamp(value - 1, 0, rate);
    if (value == 0) {
      value = rate;
      return true;
    }
    return false;
  }

  int getRate() {
    return rate;
  }

  void setRate(int rate) {
    this.rate = rate;
  }
}
