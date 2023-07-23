package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class IndirectXAddress implements Address<Byte> {

  private final Ram ram;
  private final byte zeropage;
  private final XIndex index;

  public IndirectXAddress(Ram ram, byte zeropage, XIndex index) {
    this.ram = ram;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%02x,X)", zeropage);
  }

  @Override
  public Byte fetch() {
    short address = ram.fetchZeropage(zeropage, index, Short.class);
    return ram.fetch(address, Byte.class);
  }

  @Override
  public void store(Byte value) {
    short address = ram.fetchZeropage(zeropage, index, Short.class);
    ram.store(address, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
