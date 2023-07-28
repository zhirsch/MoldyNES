package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jsr extends Instruction {

  private final ReadableAddress<UInt16> address;

  public Jsr(ReadableAddress<UInt16> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "JSR $" + address.fetch();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.pushWord(UInt16.cast(regs.pc.address().value() - 1));
    regs.pc.set(address.fetch());
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
