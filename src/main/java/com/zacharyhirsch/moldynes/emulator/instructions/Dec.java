package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public class Dec extends Instruction {

  private final Address<UInt8> address;

  public Dec(Address<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.dec(address.fetch());
    address.store(result.output());
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
