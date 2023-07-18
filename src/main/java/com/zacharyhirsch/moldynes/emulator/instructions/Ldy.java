package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Ldy implements Instruction {

  public static final class Immediate extends Ldy {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("LDY #$%02x", immediate);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      regs.y = immediate;

      regs.sr.set(Registers.STATUS_REGISTER_N, regs.y < 0);
      regs.sr.set(Registers.STATUS_REGISTER_Z, regs.y == 0);
    }
  }
}
