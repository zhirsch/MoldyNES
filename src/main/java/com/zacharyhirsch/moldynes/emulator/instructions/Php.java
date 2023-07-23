package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Php implements Instruction {

  public Php(Implicit ignored) {}

  @Override
  public String toString() {
    return "PHP";
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    ram.push(regs.sr.toByte(), Byte.class);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
