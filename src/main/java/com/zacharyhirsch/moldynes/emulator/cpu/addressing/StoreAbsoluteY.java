package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class StoreAbsoluteY implements NesCpuCycle {

  private final StoreInstruction instruction;

  public StoreAbsoluteY(StoreInstruction instruction) {
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
    byte adl = (byte) (cpu.state.hold + cpu.state.y);
    if (Byte.toUnsignedInt(cpu.state.hold) + Byte.toUnsignedInt(cpu.state.y) > 255) {
      cpu.fetch((byte) (cpu.state.data + 1), adl);
    } else {
      cpu.fetch(cpu.state.data, adl);
    }
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.store(cpu.state.adh, cpu.state.adl, instruction.execute(cpu, cpu.state.data, cpu.state.hold));
    return this::cycle5;
  }

  private NesCpuCycle cycle5(NesCpu cpu) {
    return cpu.next();
  }
}
