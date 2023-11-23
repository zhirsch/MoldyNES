package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Isb implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = cpu.alu.inc(value).output();
    var result = cpu.alu.sub(cpu.state.a, output, cpu.state.pC());
    cpu.state.a = result.output();
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
    cpu.state.pC(result.c());
    cpu.state.pV(result.v());
    return output;
  }
}
