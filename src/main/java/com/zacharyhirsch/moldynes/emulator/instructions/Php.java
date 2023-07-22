package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Php implements Instruction {

  @Override
  public String toString() {
    return "PHP";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    stack.push(regs.sr.toByte());
  }
}
