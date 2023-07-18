package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Stx implements Instruction {

  public static class Zeropage implements Instruction {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STX $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropage(zeropage, regs.x);
    }
  }

  public static class ZeropageY implements Instruction {

    private final byte zeropage;

    public ZeropageY(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("STX $%02x,Y", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putZeropageIndexed(zeropage, regs.y, regs.x);
    }
  }

  public static class Absolute implements Instruction {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("STX $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      ram.putAbsolute(absolute, regs.x);
    }
  }
}
