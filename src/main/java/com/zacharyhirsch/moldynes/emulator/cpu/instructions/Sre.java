package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Sre implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) (Byte.toUnsignedInt(value) >>> 1);
    cpu.state.a = (byte) (cpu.state.a ^ output);
    cpu.state.pN(cpu.state.a < 0);
    cpu.state.pZ(cpu.state.a == 0);
    cpu.state.pC((value & 1) == 1);
    return output;
  }
}
