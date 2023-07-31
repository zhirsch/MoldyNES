package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public class Inc extends Instruction {

  private final Address<UInt8> address;

  public Inc(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.inc(address.fetch());
    address.store(result.output());
    regs.p.n = result.n();
    regs.p.z = result.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
