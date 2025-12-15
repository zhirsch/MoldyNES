package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Dcp implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) (value - 1);
    var cmp = NesAlu.cmp(cpu.state.a, output);
    cpu.state.p.n(cmp.n());
    cpu.state.p.z(cmp.z());
    cpu.state.p.c(cmp.c());
    return output;
  }
}
