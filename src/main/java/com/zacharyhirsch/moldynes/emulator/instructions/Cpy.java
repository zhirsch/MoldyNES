package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public abstract class Cpy implements Instruction {

  public static final class Immediate extends Cpy {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("CPY #$%02x", immediate);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.y - immediate);
      updateSr(regs, result);
    }
  }

  public static final class Zeropage extends Cpy {

    private final byte zeropage;

    public Zeropage(byte zeropage) {
      this.zeropage = zeropage;
    }

    @Override
    public String describe() {
      return String.format("CPY $%02x", zeropage);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.y - ram.getZeropage(zeropage));
      updateSr(regs, result);
    }
  }

  public static final class Absolute extends Cpy {

    private final short absolute;

    public Absolute(short absolute) {
      this.absolute = absolute;
    }

    @Override
    public String describe() {
      return String.format("CPY $%04x", absolute);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte result = (byte) (regs.y - ram.getAbsolute(absolute));
      updateSr(regs, result);
    }
  }

  private static void updateSr(Registers regs, byte result) {
    regs.sr.n = result < 0;
    regs.sr.z = result == 0;
    regs.sr.c = result >= 0;
  }
}
