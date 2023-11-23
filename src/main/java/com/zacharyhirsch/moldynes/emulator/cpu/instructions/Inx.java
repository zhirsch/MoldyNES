package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Inx implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = cpu.alu.inc(cpu.state.x);
    cpu.state.x = result.output();
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
  }
}
