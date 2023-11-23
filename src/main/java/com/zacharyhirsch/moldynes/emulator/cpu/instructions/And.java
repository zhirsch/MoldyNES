package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class And implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = cpu.alu.and(cpu.state.a, cpu.state.data);
    cpu.state.a = result.output();
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
  }
}
