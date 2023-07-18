package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Cpx implements Instruction {

  public static final class Immediate extends Cpx {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("CPX #$%02x", immediate);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.x - immediate);
      updateSr(regs, result);
    }
  }

  public static final class Zeropage extends Cpx {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("CPX $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.x - ram.getZeropage(zeropage));
      updateSr(regs, result);
    }
  }

  public static final class Absolute extends Cpx {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("CPX $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.x - ram.getAbsolute(absolute));
      updateSr(regs, result);
    }
  }

  private static void updateSr(Registers regs, byte result) {
    regs.sr.n = result < 0;
    regs.sr.z = result == 0;
    regs.sr.c = result >= 0;
  }
}
