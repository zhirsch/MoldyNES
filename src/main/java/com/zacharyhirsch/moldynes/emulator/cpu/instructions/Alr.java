package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Alr implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    byte input = (byte) (cpu.state.a & cpu.state.data);
    cpu.state.a = (byte) (Byte.toUnsignedInt(input) >>> 1);
    cpu.state.p.n(false);
    cpu.state.p.z(cpu.state.a == 0);
    cpu.state.p.c((input & 1) == 1);
  }
}
