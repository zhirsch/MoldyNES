package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Undocumented implements NesCpuCycle {

  private final NesCpuCycle cycle;

  public Undocumented(NesCpuCycle cycle) {
    this.cycle = cycle;
  }

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    return cycle.start(cpu, state);
  }
}
