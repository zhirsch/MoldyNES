package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.addressing.ReadModifyWriteInstruction;

public final class Sre implements ReadModifyWriteInstruction {

  @Override
  public byte execute(NesCpu cpu, byte value) {
    var lsr = cpu.alu.lsr(value);
    cpu.state.pC(lsr.c());

    var eor = cpu.alu.xor(cpu.state.a, lsr.output());
    cpu.state.a = eor.output();
    cpu.state.pN(eor.n());
    cpu.state.pZ(eor.z());

    return lsr.output();
  }
}
