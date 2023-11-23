package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;

public final class NesMmu {

  private final NesCpuMemoryMap memory;

  public boolean write;

  public NesMmu(NesCpuMemoryMap memory) {
    this.memory = memory;
    this.write = false;
  }

  public void execute(NesCpu cpu) {
    if (write) {
      memory.store(cpu.state.adh, cpu.state.adl, cpu.state.data);
    } else {
      cpu.state.data = memory.fetch(cpu.state.adh, cpu.state.adl);
    }
  }
}
