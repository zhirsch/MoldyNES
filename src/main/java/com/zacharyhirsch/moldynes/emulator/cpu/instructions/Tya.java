package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Tya implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.a = cpu.state.y;
    cpu.state.pN(cpu.state.a < 0);
    cpu.state.pZ(cpu.state.a == 0);
  }
}
