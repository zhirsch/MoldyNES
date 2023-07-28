package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Ldy extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Ldy(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.y = address.fetch();

    regs.sr.n = regs.y.bit(7) == 1;
    regs.sr.z = regs.y.isZero();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
