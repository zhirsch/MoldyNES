package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

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
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - immediate);
      updateSr(regs, result);
    }
  }

  public static final class Zeropage extends Cmp {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("CMP $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - ram.getZeropage(zeropage));
      updateSr(regs, result);
    }
  }

  public static final class ZeropageX extends Cmp {

    private final byte zeropage;

    public ZeropageX(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("CMP $%02x,X", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - ram.getZeropageIndexed(zeropage, regs.x));
      updateSr(regs, result);
    }
  }

  public static final class Absolute extends Cmp {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("CMP $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - ram.getAbsolute(absolute));
      updateSr(regs, result);
    }
  }

  public static final class AbsoluteX extends Cmp {

    private final short absolute;

    public AbsoluteX(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("CMP $%04x,X", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - ram.getAbsoluteIndexed(absolute, regs.x));
      updateSr(regs, result);
    }
  }

  public static final class AbsoluteY extends Cmp {

    private final short absolute;

    public AbsoluteY(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("CMP $%04x,Y", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.ac - ram.getAbsoluteIndexed(absolute, regs.y));
      updateSr(regs, result);
    }
  }

  private static void updateSr(Registers regs, byte result) {
    regs.sr.n = result < 0;
    regs.sr.z = result == 0;
    regs.sr.c = result >= 0;
  }
}
