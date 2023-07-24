package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Lda implements Instruction {

  private final ReadableAddress<Byte> address;

  public Lda(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.a = address.fetch();

    regs.sr.n = regs.a < 0;
    regs.sr.z = regs.a == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
