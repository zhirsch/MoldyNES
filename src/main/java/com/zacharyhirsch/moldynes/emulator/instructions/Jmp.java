package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.HaltException;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jmp implements Instruction {

  private final ReadableAddress<Short> address;

  public Jmp(ReadableAddress<Short> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    if (address instanceof IndirectAddress) {
      return "JMP " + address;
    }
    return String.format("JMP $%04X", address.fetch());
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    short dst = address.fetch();
    if (dst == regs.pc - 3) {
      throw new HaltException();
    }
    regs.pc = dst;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
