package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Arr implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    byte value = (byte) (cpu.state.a & cpu.state.data);
    cpu.state.a = (byte) ((Byte.toUnsignedInt(value) >>> 1) | (cpu.state.pC() ? 0b1000_0000 : 0));
    cpu.state.pN(cpu.state.a < 0);
    cpu.state.pZ(cpu.state.a == 0);
    cpu.state.pC(bit(value, 7) == 1);
    cpu.state.pV((bit(value, 6) ^ bit(value, 7)) == 1);
  }

  private static int bit(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }
}
