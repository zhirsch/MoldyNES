package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jsr implements Instruction {

  private final ReadableAddress<UInt16> address;

  public Jsr(ReadableAddress<UInt16> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return String.format("JSR $%s", address.fetch());
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.pushWord(regs.pc.dec().address());
    regs.pc = new ProgramCounter(address.fetch());
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
