package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Rra implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    var ror = cpu.alu.ror(value, cpu.state.pC());

    var add = cpu.alu.add(cpu.state.a, ror.output(), ror.c());
    cpu.state.a = add.output();
    cpu.state.pN(add.n());
    cpu.state.pZ(add.z());
    cpu.state.pC(add.c());
    cpu.state.pV(add.v());

    return ror.output();
  }
}
