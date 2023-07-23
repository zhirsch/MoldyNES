package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Pha implements Instruction {

  public Pha(Implicit ignored) {}

  @Override
  public String toString() {
    return "PHA";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    ram.push(regs.ac, Byte.class);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
