package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuHaltException;

final class Emulator {

  private final NesCpu cpu;

  public Emulator(NesCpuMemoryMap memory) {
    this.cpu = new NesCpu(memory);
  }

  public void run() {
    try {
      for (int counter = 0; ; counter++) {
        cpu.tick();
      }
    } catch (NesCpuHaltException exc) {
      return;
    }
  }
}
