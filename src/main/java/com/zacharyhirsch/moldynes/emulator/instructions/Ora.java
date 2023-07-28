package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Ora extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Ora(ReadableAddress<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.or(regs.a, address.fetch());
    regs.a = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
