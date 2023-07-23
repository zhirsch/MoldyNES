package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Sed implements Instruction {

  public Sed(Implicit ignored) {}

  @Override
  public String toString() {
    return "SED";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.d = true;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
