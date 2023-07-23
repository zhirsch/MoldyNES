package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Asl implements Instruction {

  private final Address<Byte> address;

  public Asl(Address<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    byte input = address.fetch();
    byte output = (byte) (Byte.toUnsignedInt(input) << 1);
    address.store(output);

    regs.sr.c = input < 0;
    regs.sr.n = output < 0;
    regs.sr.z = output == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
