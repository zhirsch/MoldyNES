package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

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
    public void execute(ByteBuffer ram, Registers regs) {
      regs.pc = absolute;
    }
  }
}
