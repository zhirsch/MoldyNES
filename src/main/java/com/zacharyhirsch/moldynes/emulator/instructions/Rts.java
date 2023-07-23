package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rts implements Instruction {

  public Rts(Implicit ignored) {}

  @Override
  public String toString() {
    return "RTS";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.pc = (short) (ram.pull(Short.class) + 1);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
