package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.NesAlu.Result;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Slo extends Instruction {

  private final Address<UInt8> address;

  public Slo(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    Result asl = NesAlu.asl(address.fetch());
    address.store(asl.output());
    regs.sr.c = asl.c();

    NesAlu.Result or = NesAlu.or(regs.a, asl.output());
    regs.a = or.output();
    regs.sr.n = or.n();
    regs.sr.z = or.z();
  }

  @Override
  public Argument getArgument() {
    return address;
  }

}
