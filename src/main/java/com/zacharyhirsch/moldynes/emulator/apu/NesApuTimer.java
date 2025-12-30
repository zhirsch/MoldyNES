package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuTimer {

  private int rate;
  private int pendingRate;
  private int value;

  NesApuTimer() {
    this.rate = 0;
    this.value = 0;
  }

  boolean tick() {
    if (rate == 0) {
      rate = pendingRate;
      return false;
    }
    value--;
    if (value <= 0) {
      rate = pendingRate;
      value = rate;
      return true;
    }
    return false;
  }

  public int getRate() {
    return rate;
  }

  public void setRateLo(int lo) {
    this.pendingRate = (pendingRate & 0b1111_1111_0000_0000) | (lo << 0);
  }

  public void setRateHi(int hi) {
    this.pendingRate = (pendingRate & 0b0000_0000_1111_1111) | (hi << 8);
  }
}
