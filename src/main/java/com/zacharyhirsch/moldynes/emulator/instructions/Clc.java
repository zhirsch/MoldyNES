package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Clc implements Instruction {

  public Clc(Implicit ignored) {}

  @Override
  public String toString() {
    return "CLC";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.sr.c = false;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
