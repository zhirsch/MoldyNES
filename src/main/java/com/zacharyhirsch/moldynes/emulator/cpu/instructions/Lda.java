package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Lda implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.a = cpu.state.data;
    cpu.state.p.n(cpu.state.data < 0);
    cpu.state.p.z(cpu.state.data == 0);
  }
}
