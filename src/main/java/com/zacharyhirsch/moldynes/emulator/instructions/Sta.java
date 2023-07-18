package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Sta implements Instruction {

  public static class Zeropage implements Instruction {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STA $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropage(zeropage, regs.ac);
    }
  }

  public static class ZeropageX implements Instruction {

    private final byte zeropage;

    public ZeropageX(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STA $%02x,X", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropageIndexed(zeropage, regs.x, regs.ac);
    }
  }

  public static final class Absolute extends Sta {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("STA #$%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putAbsolute(absolute, regs.ac);
    }
  }

  public static final class AbsoluteX extends Sta {

    private final short absolute;

    public AbsoluteX(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("STA #$%04x,X", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putAbsoluteIndexed(absolute, regs.x, regs.ac);
    }
  }

  public static final class AbsoluteY extends Sta {

    private final short absolute;

    public AbsoluteY(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("STA #$%04x,Y", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putAbsoluteIndexed(absolute, regs.y, regs.ac);
    }
  }
}
