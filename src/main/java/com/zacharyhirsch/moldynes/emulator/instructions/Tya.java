package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tya extends Instruction {

  private final Implicit implicit;

  public Tya(Implicit implicit) {
        this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.a = regs.y;

    regs.p.n = regs.a.bit(7) == 1;
    regs.p.z = regs.a.isZero();
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }

}
