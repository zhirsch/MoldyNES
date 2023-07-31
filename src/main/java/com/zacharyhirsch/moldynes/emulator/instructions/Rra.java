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
    NesAlu.Result ror = NesAlu.ror(address.fetch(), regs.p.c);
    address.store(ror.output());

    NesAlu.Result add = NesAlu.add(regs.a, ror.output(), ror.c());
    regs.a = add.output();
    regs.p.n = add.n();
    regs.p.z = add.z();
    regs.p.c = add.c();
    regs.p.v = add.v();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
