package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Sre extends Instruction {

  private final Address<UInt8> address;

  public Sre(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result lsr = NesAlu.lsr(address.fetch());
    address.store(lsr.output());
    regs.sr.c = lsr.c();

    NesAlu.Result eor = NesAlu.xor(regs.a, lsr.output());
    regs.a = eor.output();
    regs.sr.n = eor.n();
    regs.sr.z = eor.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
