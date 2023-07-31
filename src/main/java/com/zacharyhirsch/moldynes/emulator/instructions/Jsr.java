package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateWord;

public final class Jsr extends Instruction {

  private final ImmediateWord address;

  public Jsr(ImmediateWord address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "JSR $" + address.fetch();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt16 pc = UInt16.cast(regs.pc.address().value() - 1);
    stack.pushByte(pc.msb());
    stack.pushByte(pc.lsb());
    regs.pc.set(address.fetch());
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
