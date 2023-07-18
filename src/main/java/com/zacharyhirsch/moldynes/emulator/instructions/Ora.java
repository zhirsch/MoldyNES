package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Ora implements Instruction {

  public static class Immediate implements Instruction {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("ORA #$%02x", immediate);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = (byte) (regs.ac | immediate);

      regs.sr.n = regs.ac < 0;
      regs.sr.z = regs.ac == 0;
    }
  }
}
