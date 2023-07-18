package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public class Tax implements Instruction {

  @Override
  public String describe() {
    return "TAX";
  }

  @Override
  public void execute(ByteBuffer ram, Registers regs) {
    regs.x = regs.ac;

    regs.sr.set(Registers.STATUS_REGISTER_N, regs.x < 0);
    regs.sr.set(Registers.STATUS_REGISTER_Z, regs.x == 0);
  }
}
