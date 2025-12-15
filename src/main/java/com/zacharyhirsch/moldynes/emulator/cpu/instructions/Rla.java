package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Rla implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) ((value << 1) | (cpu.state.p.c() ? 1 : 0));
    cpu.state.a = (byte) (cpu.state.a & output);
    cpu.state.p.n(cpu.state.a < 0);
    cpu.state.p.z(cpu.state.a == 0);
    cpu.state.p.c(value < 0);
    return output;
  }
}
