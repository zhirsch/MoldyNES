package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Lda extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Lda(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.a = address.fetch();

    regs.p.n = regs.a.bit(7) == 1;
    regs.p.z = regs.a.isZero();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
