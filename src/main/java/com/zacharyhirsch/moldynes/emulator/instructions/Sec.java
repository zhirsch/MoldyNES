package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;

public class Sec implements Instruction {

  public Sec() {}

  @Override
  public String toString() {
    return "SEC";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.sr.c = true;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
