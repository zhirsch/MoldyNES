package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Ror extends Instruction {

  private final Address<UInt8> address;

  public Ror(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.ror(address.fetch(), regs.sr.c);
    address.store(result.output());
    regs.sr.c = result.c();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
