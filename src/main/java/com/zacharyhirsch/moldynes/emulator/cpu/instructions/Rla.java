package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Rla implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    var rol = cpu.alu.rol(value, cpu.state.pC());
    cpu.state.pC(rol.c());

    var and = cpu.alu.and(cpu.state.a, rol.output());
    cpu.state.a = and.output();
    cpu.state.pN(and.n());
    cpu.state.pZ(and.z());

    return rol.output();
  }
}
