package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Sty implements Instruction {

  public static class Zeropage implements Instruction {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STY $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropage(zeropage, regs.y);
    }
  }

  public static class ZeropageX implements Instruction {

    private final byte zeropage;

    public ZeropageX(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STY $%02x,X", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropageIndexed(zeropage, regs.x, regs.y);
    }
  }

  public static class Absolute implements Instruction {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("STY $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putAbsolute(absolute, regs.y);
    }
  }
}
