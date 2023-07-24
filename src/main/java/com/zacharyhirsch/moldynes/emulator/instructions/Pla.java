package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Pla implements Instruction {

  public Pla(Implicit ignored) {}

  @Override
  public String toString() {
    return "PLA";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.a = stack.pull(Byte.class);

    regs.sr.n = regs.a < 0;
    regs.sr.z = regs.a == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
