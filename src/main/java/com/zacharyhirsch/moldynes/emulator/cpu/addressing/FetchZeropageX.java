package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class FetchZeropageX implements NesCpuCycle {

  private final FetchInstruction instruction;

  public FetchZeropageX(FetchInstruction instruction) {
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
    cpu.fetch((byte) 0x00, cpu.state.data);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.fetch((byte) 0x00, (byte) (cpu.state.adl + cpu.state.x));
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    instruction.execute(cpu);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
