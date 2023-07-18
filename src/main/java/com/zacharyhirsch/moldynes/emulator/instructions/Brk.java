package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Brk implements Instruction {

  @Override
  public String describe() {
    return "BRK";
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    stack.pushShort((short) (regs.pc + 1));
    stack.push(regs.sr.toByte());
    regs.sr.i = true;
    regs.pc = ram.getShortAbsolute(Ram.NMI_VECTOR);
  }
}
