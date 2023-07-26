package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.WritableAddress;

public final class Sty implements Instruction {

  private final WritableAddress<UInt8> address;

  public Sty(WritableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    address.store(regs.y);
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
