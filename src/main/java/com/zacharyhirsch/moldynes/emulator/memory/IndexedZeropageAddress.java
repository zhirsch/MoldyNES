package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class IndexedZeropageAddress implements ReadableAddress<Byte>, WritableAddress<Byte> {

  private final Ram ram;
  private final byte zeropage;
  private final Index index;

  public IndexedZeropageAddress(Ram ram, byte zeropage, Index index) {
    this.ram = ram;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%02x,%s", zeropage, index);
  }

  @Override
  public Byte fetch() {
    return ram.fetchZeropage(zeropage, index, Byte.class);
  }

  @Override
  public void store(Byte value) {
    ram.storeZeropage(zeropage, index, value);
  }
}
