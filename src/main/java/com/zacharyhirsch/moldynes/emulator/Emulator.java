package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuHaltException;

final class Emulator {

  private final NesCpu cpu;

  public Emulator(NesCpuMemoryMap memory) {
    this.cpu = new NesCpu(memory);
  }

  public void run() {
    while (true) {
      try {
        cpu.tick();
      } catch (NesCpuHaltException exc) {
        break;
      }
    }
  }
}
