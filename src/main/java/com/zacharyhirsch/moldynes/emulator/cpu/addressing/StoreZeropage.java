package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class StoreZeropage implements NesCpuCycle {

  private final StoreInstruction instruction;

  public StoreZeropage(StoreInstruction instruction) {
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
    cpu.store((byte) 0x00, cpu.state.data, instruction.execute(cpu, cpu.state.data, cpu.state.hold));
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    return cpu.next();
  }
}
