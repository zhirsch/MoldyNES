package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuInterrupt;

public class Brk implements NesCpuCycle {

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    NesCpuCycle next = new NesCpuInterrupt((short) 0xfffe, (short) 0xffff, true).execute(cpu);
    cpu.state.pc++;
    return next;
  }
}
