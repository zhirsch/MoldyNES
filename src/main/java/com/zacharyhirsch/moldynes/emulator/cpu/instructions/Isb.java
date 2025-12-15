package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Isb implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) (value + 1);
    var result = NesAlu.sub(cpu.state.a, output, cpu.state.p.c());
    cpu.state.a = result.output();
    cpu.state.p.n(result.n());
    cpu.state.p.z(result.z());
    cpu.state.p.c(result.c());
    cpu.state.p.v(result.v());
    return output;
  }
}
