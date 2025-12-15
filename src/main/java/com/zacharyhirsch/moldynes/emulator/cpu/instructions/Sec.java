package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Sec implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.p.c(true);
  }
}
