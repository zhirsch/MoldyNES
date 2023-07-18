package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Bne implements Instruction {

  public static final class Relative extends Bne {

    private final byte relative;

    public Relative(byte relative) {
      this.relative = relative;
    }

    @Override
    public String describe() {
      return String.format("BNE #$%02x", relative);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      if (!regs.sr.z) {
        regs.pc += relative;
      }
    }
  }
}
