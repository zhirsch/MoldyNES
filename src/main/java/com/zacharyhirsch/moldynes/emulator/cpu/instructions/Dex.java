package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Dex implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.x = (byte) (cpu.state.x - 1);
    cpu.state.pN(cpu.state.x < 0);
    cpu.state.pZ(cpu.state.x == 0);
  }
}
