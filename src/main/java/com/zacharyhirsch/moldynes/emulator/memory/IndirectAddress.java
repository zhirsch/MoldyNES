package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class IndirectAddress implements ReadableAddress<Short> {

  private final Ram ram;
  private final short absolute;

  public IndirectAddress(Ram ram, short absolute) {
    this.ram = ram;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("($%04x)", absolute);
  }

  @Override
  public Short fetch() {
    return ram.fetch(absolute, Short.class);
  }
}
