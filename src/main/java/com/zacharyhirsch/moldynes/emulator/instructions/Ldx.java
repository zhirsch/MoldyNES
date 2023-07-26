package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Ldx implements Instruction {

  private final ReadableAddress<UInt8> address;

  public Ldx(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x = address.fetch();

    regs.sr.n = regs.x.bit(7) == 1;
    regs.sr.z = regs.x.isZero();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
