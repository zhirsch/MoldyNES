package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Dex extends Instruction {

  private final Implicit implicit;

  public Dex(Implicit implicit) {
        this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.dec(regs.x);
    regs.x = result.output();
    regs.p.n = result.n();
    regs.p.z = result.z();
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }

}
