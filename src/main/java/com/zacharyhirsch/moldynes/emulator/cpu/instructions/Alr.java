package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Alr implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var and = cpu.alu.and(cpu.state.a, cpu.state.data);
    var lsr = cpu.alu.lsr(and.output());
    cpu.state.a = lsr.output();
    cpu.state.pN(lsr.n());
    cpu.state.pZ(lsr.z());
    cpu.state.pC(lsr.c());
  }
}
