package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Tay implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.y = cpu.state.a;
    cpu.state.pN(cpu.state.y < 0);
    cpu.state.pZ(cpu.state.y == 0);
  }
}
