package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Sed implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.pD(true);
  }
}
