package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;

public class Php implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1();
  }

  private NesCpuCycle cycle1() {
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    cpu.store((byte) 0x01, cpu.state.sp--, cpu.state.p.toByte(true));
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    cpu.fetch(cpu.state.pc++);
    return cpu::next;
  }
}
