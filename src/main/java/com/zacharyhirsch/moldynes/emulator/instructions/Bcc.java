package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Bcc implements Instruction {

  public static final class Relative extends Bcc {

    private final byte relative;

    public Relative(byte relative) {
      this.relative = relative;
    }

    @Override
    public String describe() {
      return String.format("BCC #$%02x", relative);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      if (!regs.sr.get(Registers.STATUS_REGISTER_C)) {
        regs.pc += relative;
      }
    }
  }
}
