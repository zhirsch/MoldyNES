package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Cli implements Instruction {

  public Cli(Implicit ignored) {}

  @Override
  public String toString() {
    return "CLI";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.i = false;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
