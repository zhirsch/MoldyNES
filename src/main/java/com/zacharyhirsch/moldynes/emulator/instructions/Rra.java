package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Rra extends Instruction {

  private final Address<UInt8> address;

  public Rra(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result ror = NesAlu.ror(address.fetch(), regs.sr.c);
    address.store(ror.output());

    NesAlu.Result add = NesAlu.add(regs.a, ror.output(), ror.c());
    regs.a = add.output();
    regs.sr.n = add.n();
    regs.sr.z = add.z();
    regs.sr.c = add.c();
    regs.sr.v = add.v();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
