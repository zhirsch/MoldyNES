package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Cmp implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = cpu.alu.sub(cpu.state.a, cpu.state.data);
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
    cpu.state.pC(result.c());
  }
}
