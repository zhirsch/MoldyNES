package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Txs implements Instruction {

  public Txs(Implicit ignored) {}

  @Override
  public String toString() {
    return "TXS";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sp = regs.x;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
