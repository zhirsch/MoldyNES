package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class AbsoluteAddress implements Address<Byte> {

  private final Ram ram;
  private final short absolute;

  public AbsoluteAddress(Ram ram, short absolute) {
    this.ram = ram;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("$%04x", absolute);
  }

  @Override
  public Byte fetch() {
    return ram.fetch(absolute, Byte.class);
  }

  @Override
  public void store(Byte value) {
    ram.store(absolute, value);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
