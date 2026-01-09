package com.zacharyhirsch.moldynes.emulator;


public final class NesClock {

  private long cycle;

  public NesClock() {
    this.cycle = 1;
    //    MDC.put("cycle", "%10d".formatted(cycle));
  }

  public void tick() {
    cycle++;
    //    MDC.put("cycle", "%10d".formatted(cycle));
  }

  public long getCycle() {
    return cycle;
  }
}
