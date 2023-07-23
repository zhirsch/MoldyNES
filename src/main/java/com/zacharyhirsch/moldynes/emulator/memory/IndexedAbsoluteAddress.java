package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class IndexedAbsoluteAddress implements Address<Byte> {

  private final Ram ram;
  private final short absolute;
  private final Index index;

  public IndexedAbsoluteAddress(Ram ram, short absolute, Index index) {
    this.ram = ram;
    this.absolute = absolute;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%04x%s", absolute, index);
  }

  @Override
  public Byte fetch() {
    return ram.fetch(absolute, index, Byte.class);
  }

  @Override
  public void store(Byte value) {
    ram.store(absolute, index, value);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
