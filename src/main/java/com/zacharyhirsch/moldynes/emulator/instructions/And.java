package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class And implements Instruction {

  private final ReadableAddress<UInt8> address;

  public And(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.and(regs.a, address.fetch());
    regs.a = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
