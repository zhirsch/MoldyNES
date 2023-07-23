package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Cld implements Instruction {

  public Cld(Implicit ignored) {}

  @Override
  public String toString() {
    return "CLD";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.d = false;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
