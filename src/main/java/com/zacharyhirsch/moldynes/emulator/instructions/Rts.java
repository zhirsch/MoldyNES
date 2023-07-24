package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rts implements Instruction {

  public Rts(Implicit ignored) {}

  @Override
  public String toString() {
    return "RTS";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.pc = (short) (stack.pull(Short.class) + 1);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
