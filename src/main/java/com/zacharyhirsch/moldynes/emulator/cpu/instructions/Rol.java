package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Rol implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    var result = cpu.alu.rol(value, cpu.state.pC());
    cpu.state.pN(result.n());
    cpu.state.pZ(result.z());
    cpu.state.pC(result.c());
    return result.output();
  }
}
