package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Lsr implements Instruction {

  private final Address<UInt8> address;

  public Lsr(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    byte input = address.fetch();
    byte output = (byte) (Byte.toUnsignedInt(input) >>> 1);
    address.store(output);

    regs.sr.c = (input & 0b0000_0001) == 0b0000_0001;
    regs.sr.n = false;
    regs.sr.z = output == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
