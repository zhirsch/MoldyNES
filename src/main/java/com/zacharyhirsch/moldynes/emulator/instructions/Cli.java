package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Cli implements Instruction {

  @Override
  public String toString() {
    return "CLI";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.sr.i = false;
  }
}
