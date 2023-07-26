package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tay implements Instruction {

  public Tay(Implicit ignored) {}

  @Override
  public String toString() {
    return "TAY";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.y = regs.a;
    regs.sr.n = regs.y.bit(7) == 1;
    regs.sr.z = regs.y.isZero();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
