package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Tya implements Instruction {

  @Override
  public String toString() {
    return "TYA";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.ac = regs.y;

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }
}
