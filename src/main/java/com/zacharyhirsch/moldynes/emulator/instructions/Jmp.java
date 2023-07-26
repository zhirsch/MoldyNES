package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jmp implements Instruction {

  private final ReadableAddress<UInt16> address;

  public Jmp(ReadableAddress<UInt16> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    if (address instanceof IndirectAddress) {
      return "JMP " + address;
    }
    return String.format("JMP $%s", address.fetch());
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.pc = new ProgramCounter(address.fetch());
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
