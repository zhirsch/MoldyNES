package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Txa implements Instruction {

  public Txa(Implicit ignored) {}

  @Override
  public String toString() {
    return "TXA";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.ac = regs.x;

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
