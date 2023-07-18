package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Eor implements Instruction {

  public static class Immediate implements Instruction {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("EOR #$%02x", immediate);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      regs.ac = (byte) (regs.ac ^ immediate);

      regs.sr.set(Registers.STATUS_REGISTER_N, regs.ac < 0);
      regs.sr.set(Registers.STATUS_REGISTER_Z, regs.ac == 0);
    }
  }
}
