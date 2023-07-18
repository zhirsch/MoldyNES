package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Lda implements Instruction {

  public static final class Immediate extends Lda {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("LDA #$%02x", immediate);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      regs.ac = immediate;
      updateSr(regs);
    }
  }

  public static class Absolute implements Instruction {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("LDA #$%04x", absolute);
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
      regs.ac = ram.get(absolute);
      updateSr(regs);
    }
  }

  private static void updateSr(Registers regs) {
    regs.sr.set(Registers.STATUS_REGISTER_N, regs.ac < 0);
    regs.sr.set(Registers.STATUS_REGISTER_Z, regs.ac == 0);
  }
}
