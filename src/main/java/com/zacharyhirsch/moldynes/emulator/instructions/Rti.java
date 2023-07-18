package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Rti implements Instruction {

  @Override
  public String describe() {
    return "RTI";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.sr.fromByte(stack.pull());
    regs.pc = stack.pullShort();
  }
}
