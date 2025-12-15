package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Bit implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.p.n((cpu.state.data & 0b1000_0000) == 0b1000_0000);
    cpu.state.p.v((cpu.state.data & 0b0100_0000) == 0b0100_0000);
    cpu.state.p.z((cpu.state.data & cpu.state.a) == 0);
  }
}
