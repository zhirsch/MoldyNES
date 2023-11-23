package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ImpliedInstruction;

public final class Iny implements ImpliedInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = cpu.alu.inc(cpu.state.y);
    cpu.state.y = result.output();
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
  }
}
