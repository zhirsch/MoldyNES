package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rts extends Instruction {

  private final Implicit implicit;

  public Rts(Implicit implicit) {
        this.implicit = implicit;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.pc.set(stack.pullWord());
    regs.pc.inc();
  }

  @Override
  public Argument getArgument() {
    return implicit;
  }

}
