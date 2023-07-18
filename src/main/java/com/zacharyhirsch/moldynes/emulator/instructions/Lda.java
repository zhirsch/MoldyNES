package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

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
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = immediate;
      updateSr(regs);
    }
  }

  public static class Zeropage implements Instruction {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("LDA $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = ram.getZeropage(zeropage);
      updateSr(regs);
    }
  }

  public static class ZeropageX implements Instruction {

    private final byte zeropage;

    public ZeropageX(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("LDA $%02x,X", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = ram.getZeropageIndexed(zeropage, regs.x);
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
      return String.format("LDA $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = ram.getAbsolute(absolute);
      updateSr(regs);
    }
  }

  public static class AbsoluteX implements Instruction {

    private final short absolute;

    public AbsoluteX(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("LDA $%04x,X", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = ram.getAbsoluteIndexed(absolute, regs.x);
      updateSr(regs);
    }
  }

  public static class AbsoluteY implements Instruction {

    private final short absolute;

    public AbsoluteY(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("LDA $%04x,Y", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.ac = ram.getAbsoluteIndexed(absolute, regs.y);
      updateSr(regs);
    }
  }

  private static void updateSr(Registers regs) {
    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }
}
