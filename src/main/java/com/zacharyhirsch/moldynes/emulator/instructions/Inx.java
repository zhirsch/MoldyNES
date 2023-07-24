package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Inx implements Instruction {

  public Inx(Implicit ignored) {}

  @Override
  public String toString() {
    return "INX";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x += 1;

    regs.sr.n = regs.x < 0;
    regs.sr.z = regs.x == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
