package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public final class Hlt implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cpu.halt();
  }
}
