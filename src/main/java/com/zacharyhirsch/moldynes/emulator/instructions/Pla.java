package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Pla implements Instruction {

  @Override
  public String describe() {
    return "PLA";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.ac = stack.pull();

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }
}
