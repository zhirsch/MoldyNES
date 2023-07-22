package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;

public final class Bvs implements Instruction {

  private final Immediate<Byte> immediate;

  public Bvs(Immediate<Byte> immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + immediate.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    if (regs.sr.v) {
      regs.pc += immediate.fetch();
    }
  }
}
