package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Ldy implements Instruction {

  private final ReadableAddress<Byte> address;

  public Ldy(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    regs.y = address.fetch();

    regs.sr.n = regs.y < 0;
    regs.sr.z = regs.y == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
