package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Atx implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.a = cpu.state.x = cpu.state.data;
    cpu.state.p.n(cpu.state.a < 0);
    cpu.state.p.z(cpu.state.a == 0);
  }
}
