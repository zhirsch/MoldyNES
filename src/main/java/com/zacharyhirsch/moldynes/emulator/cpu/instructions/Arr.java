package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.FetchInstruction;

public final class Arr implements FetchInstruction {

  @Override
  public void execute(NesCpu cpu) {
    var ror = cpu.alu.ror((byte) (cpu.state.a & cpu.state.data), cpu.state.pC());
    cpu.state.a = ror.output();
    cpu.state.pN(ror.n());
    cpu.state.pZ(ror.z());
    int bit5 = (cpu.state.a & (0b0010_0000)) >>> 5;
    int bit6 = (cpu.state.a & (0b0100_0000)) >>> 6;
    cpu.state.pC(bit6 == 1);
    cpu.state.pV((bit5 ^ bit6) == 1);
  }
}
