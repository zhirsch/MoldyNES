package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Rol extends Instruction {

  private final Address<UInt8> address;

  public Rol(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.rol(address.fetch(), regs.sr.c);
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
