package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Inx extends Instruction {

  private final Implicit implicit;

  public Inx(Implicit implicit) {
    this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.inc(regs.x);
    regs.x = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }
}
