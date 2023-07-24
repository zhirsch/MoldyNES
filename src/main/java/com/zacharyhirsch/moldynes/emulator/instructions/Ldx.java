package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Ldx implements Instruction {

  private final ReadableAddress<Byte> address;

  public Ldx(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.x = address.fetch();

    regs.sr.n = regs.x < 0;
    regs.sr.z = regs.x == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
