package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;

public class Txs implements Instruction {

  @Override
  public String describe() {
    return "TXS";
  }

  @Override
  public void execute(ByteBuffer ram, Registers regs) {
    regs.sp = regs.x;
  }
}
