package com.zacharyhirsch.moldynes.emulator;

public final class NesClock {

  private final int frequency;

  private int cycle;

  public NesClock() {
    this.frequency = 24;
    this.cycle = 0;
  }

  public boolean isOdd() {
    return cycle % 2 == 1;
  }

  public int tick() {
    int cycle = this.cycle;
    this.cycle++;
    if (this.cycle == frequency) {
      this.cycle = 0;
    }
    return cycle;
  }
}
