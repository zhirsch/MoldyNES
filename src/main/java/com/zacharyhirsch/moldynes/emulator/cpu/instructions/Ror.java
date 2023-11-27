package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Ror implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) ((Byte.toUnsignedInt(value) >>> 1) | (cpu.state.pC() ? 0b1000_0000 : 0));
    cpu.state.pN(output < 0);
    cpu.state.pZ(output == 0);
    cpu.state.pC((value & 1) == 1);
    return output;
  }
}
