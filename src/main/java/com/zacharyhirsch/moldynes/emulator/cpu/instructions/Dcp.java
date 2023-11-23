package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Dcp implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = cpu.alu.dec(value).output();
    var cmp = cpu.alu.sub(cpu.state.a, output);
    cpu.state.pN(cmp.n());
    cpu.state.pZ(cmp.z());
    cpu.state.pC(cmp.c());
    return output;
  }
}
