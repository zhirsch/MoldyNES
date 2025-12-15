package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Lsr implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) (Byte.toUnsignedInt(value) >>> 1);
    cpu.state.p.n(false);
    cpu.state.p.z(output == 0);
    cpu.state.p.c((value & 1) == 1);
    return output;
  }
}
