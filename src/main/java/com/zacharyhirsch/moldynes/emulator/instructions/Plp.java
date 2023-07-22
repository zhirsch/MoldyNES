package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Plp implements Instruction {

  @Override
  public String toString() {
    return "PLP";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.sr.fromByte(stack.pull());
  }
}
