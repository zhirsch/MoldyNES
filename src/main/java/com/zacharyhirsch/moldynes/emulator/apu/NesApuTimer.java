package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuTimer {

  private int period;
  private int pendingPeriod;
  private int value;

  NesApuTimer() {
    this.period = 0;
    this.value = 0;
  }

  boolean tick() {
    if (period == 0) {
      period = pendingPeriod;
      return false;
    }
    value--;
    if (value <= 0) {
      period = pendingPeriod;
      value = period;
      return true;
    }
    return false;
  }

  public int getPeriod() {
    return period;
  }

  public void setPeriodLo(int lo) {
    this.pendingPeriod = (pendingPeriod & 0b1111_1111_0000_0000) | (lo << 0);
  }

  public void setPeriodHi(int hi) {
    this.pendingPeriod = (pendingPeriod & 0b0000_0000_1111_1111) | (hi << 8);
  }

  public void setPeriod(int period) {
    this.pendingPeriod = period;
  }
}
