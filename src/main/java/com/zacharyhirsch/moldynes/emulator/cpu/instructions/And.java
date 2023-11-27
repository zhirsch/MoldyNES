package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class And implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    byte output = (byte) (cpu.state.a & cpu.state.data);
    cpu.state.a = output;
    cpu.state.pN(output < 0);
    cpu.state.pZ(output == 0);
  }
}
