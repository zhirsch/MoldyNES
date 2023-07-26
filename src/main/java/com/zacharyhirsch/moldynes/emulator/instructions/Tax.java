package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tax implements Instruction {

  public Tax(Implicit ignored) {}

  @Override
  public String toString() {
    return "TAX";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x = regs.a;
    regs.sr.n = regs.x.bit(7) == 1;
    regs.sr.z = regs.x.isZero();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
