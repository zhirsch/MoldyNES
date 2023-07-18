package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

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
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.x = immediate;
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
      return String.format("LDX $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.x = ram.getZeropage(zeropage);
      updateSr(regs);
    }
  }

  public static class ZeropageY implements Instruction {

    private final byte zeropage;

    public ZeropageY(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("LDX $%02x,Y", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.x = ram.getZeropageIndexed(zeropage, regs.y);
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
      return String.format("LDX $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.x = ram.getAbsolute(absolute);
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
      return String.format("LDX $%04x,Y", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      regs.x = ram.getAbsoluteIndexed(absolute, regs.y);
      updateSr(regs);
    }
  }

  private static void updateSr(Registers regs) {
    regs.sr.n = regs.x < 0;
    regs.sr.z = regs.x == 0;
  }
}
