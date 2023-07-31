package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tsx extends Instruction {

  private final Implicit implicit;

  public Tsx(Implicit implicit) {
        this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x = regs.sp;

    regs.p.n = regs.x.bit(7) == 1;
    regs.p.z = regs.x.isZero();
  }
  @Override
  public Argument getArgument() {
    return implicit;
  }

}
