package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Jsr implements Instruction {

  private final ReadableAddress<Short> address;

  public Jsr(ReadableAddress<Short> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs) {
    ram.push((short) (regs.pc - 1), Short.class);
    regs.pc = address.fetch();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
