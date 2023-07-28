package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Bit extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Bit(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 input = address.fetch();
    NesAlu.Result result = NesAlu.and(regs.a, input);
    regs.sr.z = result.z();
    regs.sr.n = input.bit(7) == 1;
    regs.sr.v = input.bit(6) == 1;
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
