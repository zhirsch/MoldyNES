package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Hlt implements NesCpuCycle {

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    return cpu.halt();
  }
}
