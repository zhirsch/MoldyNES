package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Tay implements Instruction {

  @Override
  public String toString() {
    return "TAY";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.y = regs.ac;

    regs.sr.n = regs.y < 0;
    regs.sr.z = regs.y == 0;
  }
}
