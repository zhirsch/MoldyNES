package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Ror implements Instruction {

  private final Address<Byte> address;

  public Ror(Address<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    byte input = address.fetch();
    byte output = (byte) ((Byte.toUnsignedInt(input) >>> 1) | (regs.sr.c ? 0b1000_0000 : 0));
    address.store(output);

    regs.sr.c = (input & 0b0000_0001) == 0b0000_0001;
    regs.sr.n = output < 0;
    regs.sr.z = output == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
