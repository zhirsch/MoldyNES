package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Rts implements Instruction {

  @Override
  public String describe() {
    return "RTS";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.pc = (short) (stack.pullShort() + 1);
  }
}
