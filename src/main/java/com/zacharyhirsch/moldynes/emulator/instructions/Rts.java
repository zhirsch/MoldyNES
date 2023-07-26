package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rts implements Instruction {

  public Rts(Implicit ignored) {}

  @Override
  public String toString() {
    return "RTS";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.pc = new ProgramCounter(stack.pullWord()).inc();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
