package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateWord;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jmp extends Instruction {

  private final ReadableAddress<UInt16> address;

  public Jmp(ReadableAddress<UInt16> address) {
        this.address = address;
  }

  @Override
  public String toString() {
    if (address instanceof ImmediateWord) {
      return "JMP $" + address.fetch();
    }
    return "JMP " + address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.pc.set(address.fetch());
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
