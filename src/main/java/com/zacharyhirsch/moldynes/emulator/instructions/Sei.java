package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Sei implements Instruction {

  public Sei(Implicit ignored) {}

  @Override
  public String toString() {
    return "SEI";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.i = true;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
