package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Lsr extends Instruction {

  private final Address<UInt8> address;

  public Lsr(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.lsr(address.fetch());
    address.store(result.output());
    regs.p.c = result.c();
    regs.p.n = result.n();
    regs.p.z = result.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
