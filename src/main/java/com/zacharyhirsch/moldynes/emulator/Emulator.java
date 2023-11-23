package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class Emulator {

  private final NesCpu cpu;

  public Emulator(NesCpuMemoryMap memory) {
    this.cpu = new NesCpu(memory);
  }

  public void run() {
    for (int counter = 0; ; counter++) {
      cpu.tick();
    }
  }
}
