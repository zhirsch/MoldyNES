package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Dey implements Instruction {

  public Dey(Implicit ignored) {}

  @Override
  public String toString() {
    return "DEY";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.y -= 1;

    regs.sr.n = regs.y < 0;
    regs.sr.z = regs.y == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
