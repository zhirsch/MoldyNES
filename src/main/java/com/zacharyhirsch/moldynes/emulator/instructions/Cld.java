package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public class Cld implements Instruction {

  @Override
  public String describe() {
    return "CLD";
  }

  @Override
  public void execute(ByteBuffer ram, Registers regs) {
    regs.sr.clear(Registers.STATUS_REGISTER_D);
  }
}
