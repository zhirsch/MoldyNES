package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tsx implements Instruction {

  public Tsx(Implicit ignored) {}

  @Override
  public String toString() {
    return "TSX";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x = regs.sp;

    regs.sr.n = regs.x.bit(7) == 1;
    regs.sr.z = regs.x.isZero();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
