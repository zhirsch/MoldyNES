package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public class Tya implements Instruction {

  @Override
  public String describe() {
    return "TYA";
  }

  @Override
  public void execute(ByteBuffer ram, Registers regs) {
    regs.ac = regs.y;

    regs.sr.set(Registers.STATUS_REGISTER_N, regs.ac < 0);
    regs.sr.set(Registers.STATUS_REGISTER_Z, regs.ac == 0);
  }
}
