package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public class Inc implements Instruction {

  private final Address<UInt8> address;

  public Inc(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.inc(address.fetch());
    address.store(result.output());
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
