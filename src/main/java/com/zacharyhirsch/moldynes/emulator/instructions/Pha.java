package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Pha implements Instruction {

  public Pha(Implicit ignored) {}

  @Override
  public String toString() {
    return "PHA";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.push(regs.a, Byte.class);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
