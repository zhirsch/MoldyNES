package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Tya implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.a = cpu.state.y;
    cpu.state.p.n(cpu.state.a < 0);
    cpu.state.p.z(cpu.state.a == 0);
  }
}
