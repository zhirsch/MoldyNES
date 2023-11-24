package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class StoreAbsolute implements NesCpuCycle {

  private final StoreInstruction instruction;

  public StoreAbsolute(StoreInstruction instruction) {
    this.instruction = instruction;
  }

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.state.hold = cpu.state.data;
    cpu.fetch(cpu.state.pc++);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    byte adh = cpu.state.data;
    byte adl = cpu.state.hold;
    cpu.store(adh, adl, instruction.execute(cpu, adh, adl));
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return cpu::done;
  }
}
