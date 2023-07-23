package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tya implements Instruction {

  public Tya(Implicit ignored) {}

  @Override
  public String toString() {
    return "TYA";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.ac = regs.y;

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
