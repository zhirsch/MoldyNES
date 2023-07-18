package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

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
    public void execute(ByteBuffer ram, Registers regs) {
      if (!regs.sr.get(Registers.STATUS_REGISTER_Z)) {
        regs.pc += relative;
      }
    }
  }
}
