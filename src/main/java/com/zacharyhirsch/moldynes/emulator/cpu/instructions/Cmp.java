package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Cmp implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = NesAlu.cmp(cpu.state.a, cpu.state.data);
    cpu.state.p.n(result.n());
    cpu.state.p.z(result.z());
    cpu.state.p.c(result.c());
  }
}
