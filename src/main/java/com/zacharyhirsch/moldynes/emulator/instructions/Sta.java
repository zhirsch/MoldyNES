package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public abstract class Sta implements Instruction {

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
    public void execute(ByteBuffer ram, Registers regs) {
      ram.put(absolute, regs.ac);
    }
  }
}
