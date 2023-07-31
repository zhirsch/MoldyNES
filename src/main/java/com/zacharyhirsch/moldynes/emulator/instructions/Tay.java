package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tay extends Instruction {

  private final Implicit implicit;

  public Tay(Implicit implicit) {
    this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.y = regs.a;
    regs.p.n = regs.y.bit(7) == 1;
    regs.p.z = regs.y.isZero();
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }
}
