package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Jmp implements Instruction {

  public static final class Absolute extends Jmp {
    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("JMP #$%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.pc = absolute;
    }
  }

  public static class Indirect implements Instruction {

    private final short indirect;

    public Indirect(short indirect) {
      this.indirect = indirect;
    }

    @Override
    public String describe() {
      return String.format("JMP (#$%04x)", indirect);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.pc = ram.getShortAbsolute(indirect);
    }
  }
}
