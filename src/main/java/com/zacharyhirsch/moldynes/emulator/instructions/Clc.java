package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Clc implements Instruction {

  public Clc(Implicit ignored) {}

  @Override
  public String toString() {
    return "CLC";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.c = false;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
