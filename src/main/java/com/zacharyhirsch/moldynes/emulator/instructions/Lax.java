package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Lax extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Lax(ReadableAddress<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 value = address.fetch();
    regs.a = value;
    regs.x = value;
    regs.p.n = value.bit(7) == 1;
    regs.p.z = value.isZero();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
