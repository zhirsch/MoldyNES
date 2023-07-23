package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Bit implements Instruction {

  private final ReadableAddress<Byte> address;

  public Bit(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    byte input = address.fetch();

    regs.sr.z = (regs.ac & input) == 0;
    regs.sr.n = (input & 0b1000_0000) == 0b1000_0000;
    regs.sr.v = (input & 0b0100_0000) == 0b0100_0000;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
