package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class Plp implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1();
  }

  private NesCpuCycle cycle1() {
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.fetch((byte) 0x01, cpu.state.sp++);
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.fetch((byte) 0x01, cpu.state.sp);
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    cpu.state.p.fromByte(cpu.state.data);
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
