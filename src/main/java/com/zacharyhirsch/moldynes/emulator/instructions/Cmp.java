package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Cmp implements Instruction {

  public static final class Immediate extends Cmp {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("CMP #$%02x", immediate);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      byte result = (byte) (regs.ac - immediate);

      regs.sr.set(Registers.STATUS_REGISTER_N, result < 0);
      regs.sr.set(Registers.STATUS_REGISTER_Z, result == 0);
      regs.sr.set(Registers.STATUS_REGISTER_C, result >= 0);
    }
  }
}
