package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

final class Emulator {

  private final NesCpu cpu;

  public Emulator(NesCpuMemoryMap memory, NesCpuState state) {
    this.cpu = new NesCpu(state, memory);
  }

  public void run() {
    for (int cycle = 1; ; cycle++) {
      cpu.tick();
    }
  }
}
