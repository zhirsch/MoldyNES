package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Tsx implements Instruction {

  public Tsx(Implicit ignored) {}

  @Override
  public String toString() {
    return "TSX";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.x = regs.sp;

    regs.sr.n = regs.x < 0;
    regs.sr.z = regs.x == 0;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
