package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Tax implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.x = cpu.state.a;
    cpu.state.p.n(cpu.state.x < 0);
    cpu.state.p.z(cpu.state.x == 0);
  }
}
