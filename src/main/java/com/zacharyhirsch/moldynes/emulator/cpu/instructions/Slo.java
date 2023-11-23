package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Slo implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    var asl = cpu.alu.asl(value);
    cpu.state.pC(asl.c());

    var or = cpu.alu.or(cpu.state.a, asl.output());
    cpu.state.a = or.output();
    cpu.state.pN(or.n());
    cpu.state.pZ(or.z());

    return asl.output();
  }
}
