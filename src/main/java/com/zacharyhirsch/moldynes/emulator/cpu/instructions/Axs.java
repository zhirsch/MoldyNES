package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Axs implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var result = NesAlu.cmp((byte) (cpu.state.a & cpu.state.x), cpu.state.data);
    cpu.state.x = result.output();
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
    cpu.state.pC(result.c());
  }
}
