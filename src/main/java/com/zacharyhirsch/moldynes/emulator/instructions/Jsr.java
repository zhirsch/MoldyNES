package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Jsr implements Instruction {

  public static final class Absolute extends Jsr {
    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("JSR #$%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      stack.pushShort((short) (regs.pc - 1));
      regs.pc = absolute;
    }
  }
}
