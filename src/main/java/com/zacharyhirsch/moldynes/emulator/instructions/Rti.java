package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rti implements Instruction {

  public Rti(Implicit ignored) {}

  @Override
  public String toString() {
    return "RTI";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.sr.fromByte(ram.pull(Byte.class));
    regs.pc = ram.pull(Short.class);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
