package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

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
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.y = immediate;
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
      return String.format("LDY $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.y = ram.getZeropage(zeropage);
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
      return String.format("LDY $%02x,X", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.y = ram.getZeropageIndexed(zeropage, regs.x);
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
      return String.format("LDY $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.y = ram.getAbsolute(absolute);
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
      return String.format("LDY $%04x,X", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.y = ram.getAbsoluteIndexed(absolute, regs.x);
      updateSr(regs);
    }
  }

  private static void updateSr(Registers regs) {
    regs.sr.n = regs.y < 0;
    regs.sr.z = regs.y == 0;
  }
}
