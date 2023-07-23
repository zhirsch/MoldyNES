package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;

public final class Bpl implements Instruction {

  private final Immediate<Byte> immediate;

  public Bpl(Immediate<Byte> immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + immediate.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    if (!regs.sr.n) {
      regs.pc += immediate.fetch();
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
