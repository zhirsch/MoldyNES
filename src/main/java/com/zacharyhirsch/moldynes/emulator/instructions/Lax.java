package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Lax implements Instruction {

  private final ReadableAddress<UInt8> address;

  public Lax(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 value = address.fetch();
    regs.a = value;
    regs.x = value;
    regs.sr.n = value.bit(7) == 1;
    regs.sr.z = value.isZero();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
