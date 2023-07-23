package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.WritableAddress;

public final class Sty implements Instruction {

  private final WritableAddress<Byte> address;

  public Sty(WritableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address;
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    address.store(regs.y);
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
