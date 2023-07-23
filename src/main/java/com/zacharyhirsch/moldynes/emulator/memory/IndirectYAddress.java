package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class IndirectYAddress implements Address<Byte> {

  private final Ram ram;
  private final byte zeropage;
  private final YIndex index;

  public IndirectYAddress(Ram ram, byte zeropage, YIndex index) {
    this.ram = ram;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%02x),Y", zeropage);
  }

  @Override
  public Byte fetch() {
    short base = ram.fetchZeropage(zeropage, Short.class);
    return ram.fetch(base, index, Byte.class);
  }

  @Override
  public void store(Byte value) {
    short base = ram.fetchZeropage(zeropage, Short.class);
    ram.store(base, index, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
