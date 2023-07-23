package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Nop implements Instruction {

  public Nop(Implicit ignored) {}

  @Override
  public String toString() {
    return "NOP";
  }

  @Override
  public void execute(Ram ram, Registers regs) {}

  @Override
  public int getSize() {
    return 1;
  }
}
