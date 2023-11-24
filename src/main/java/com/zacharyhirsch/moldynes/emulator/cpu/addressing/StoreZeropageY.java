package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class StoreZeropageY implements NesCpuCycle {

  private final StoreInstruction instruction;

  public StoreZeropageY(StoreInstruction instruction) {
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
    cpu.store((byte) 0x00, (byte) (cpu.state.adl + cpu.state.y), instruction.execute(cpu,
        cpu.state.data, cpu.state.hold));
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return cpu::done;
  }
}
