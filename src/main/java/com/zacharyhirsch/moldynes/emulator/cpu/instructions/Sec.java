package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Sec implements NesCpuCycle {

  @Override
  public NesCpuCycle start(NesCpu cpu, NesCpuState state) {
    cpu.fetch(state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu, NesCpuState state) {
    state.pC(true);
    return NesCpuDecode.next(cpu, state);
  }
}
