package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class FetchIndirectY implements NesCpuCycle {

  private final FetchInstruction instruction;

  public FetchIndirectY(FetchInstruction instruction) {
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
    cpu.state.hold = cpu.state.data;
    cpu.fetch((byte) 0x00, (byte) (cpu.state.adl + 1));
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.fetch(cpu.state.data, (byte) (cpu.state.hold + cpu.state.y));
    if (Byte.toUnsignedInt(cpu.state.hold) + Byte.toUnsignedInt(cpu.state.y) > 0xff) {
      return this::cycle5;
    }
    return this::cycle6;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    cpu.fetch((byte) (cpu.state.adh + 1), cpu.state.adl);
    return this::cycle6;
  }

  private NesCpuCycle cycle6(NesCpu cpu) {
    instruction.execute(cpu);
    cpu.fetch(cpu.state.pc++);
    return cpu::done;
  }
}
