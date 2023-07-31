package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Rla extends Instruction {

  private final Address<UInt8> address;

  public Rla(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result rol = NesAlu.rol(address.fetch(), regs.p.c);
    address.store(rol.output());
    regs.p.c = rol.c();

    NesAlu.Result and = NesAlu.and(regs.a, rol.output());
    regs.a = and.output();
    regs.p.n = and.n();
    regs.p.z = and.z();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
