package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Rra implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    byte output = (byte) ((Byte.toUnsignedInt(value) >> 1) | (cpu.state.p.c() ? 0b1000_0000 : 0));
    var add = NesAlu.add(cpu.state.a, output, (value & 1) == 1);
    cpu.state.a = add.output();
    cpu.state.p.n(add.n());
    cpu.state.p.z(add.z());
    cpu.state.p.c(add.c());
    cpu.state.p.v(add.v());
    return output;
  }

}
