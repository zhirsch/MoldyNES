package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Txa implements Instruction {

  public Txa(Implicit ignored) {}

  @Override
  public String toString() {
    return "TXA";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.a = regs.x;

    regs.sr.n = regs.a < 0;
    regs.sr.z = regs.a == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
