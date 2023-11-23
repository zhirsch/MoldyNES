package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Bit implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.pN((cpu.state.data & 0b1000_0000) == 0b1000_0000);
    cpu.state.pV((cpu.state.data & 0b0100_0000) == 0b0100_0000);
    cpu.state.pZ((cpu.state.data & cpu.state.a) == 0);
  }
}
