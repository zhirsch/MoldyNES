package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Inc implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) (value + 1);
    cpu.state.p.n(output < 0);
    cpu.state.p.z(output == 0);
    return output;
  }
}
