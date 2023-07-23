package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Clv implements Instruction {

  public Clv(Implicit ignored) {}

  @Override
  public String toString() {
    return "CLV";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.v = false;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
