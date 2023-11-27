package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Ora implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    cpu.state.a = (byte) (cpu.state.a | cpu.state.data);
    cpu.state.pN(cpu.state.a < 0);
    cpu.state.pZ(cpu.state.a == 0);
  }
}
