package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Sei implements Instruction {

  @Override
  public String toString() {
    return "SEI";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.sr.i = true;
  }
}
