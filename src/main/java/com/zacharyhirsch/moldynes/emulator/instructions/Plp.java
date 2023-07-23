package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Plp implements Instruction {

  public Plp(Implicit ignored) {}

  @Override
  public String toString() {
    return "PLP";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.fromByte(ram.pull(Byte.class));
  }

  @Override
  public int getSize() {
    return 1;
  }
}
