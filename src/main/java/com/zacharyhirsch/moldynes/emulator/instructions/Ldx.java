package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Ldx implements Instruction {

  public static final class Immediate extends Ldx {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("LDX #$%02x", immediate);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      regs.x = immediate;

      regs.sr.set(Registers.STATUS_REGISTER_N, regs.x < 0);
      regs.sr.set(Registers.STATUS_REGISTER_Z, regs.x == 0);
    }
  }
}
