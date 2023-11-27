package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class FetchAbsoluteX implements NesCpuCycle {

  private final FetchInstruction instruction;

  public FetchAbsoluteX(FetchInstruction instruction) {
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
    cpu.fetch(cpu.state.data, (byte) (cpu.state.hold + cpu.state.x));
    if (Byte.toUnsignedInt(cpu.state.hold) + Byte.toUnsignedInt(cpu.state.x) > 0xff) {
      return this::cycle4;
    }
    return this::cycle5;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.fetch((byte) (cpu.state.adh + 1), cpu.state.adl);
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    instruction.execute(cpu);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
