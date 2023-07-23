package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Ram;

public class ZeropageAddress implements Address<Byte> {

  private final Ram ram;
  private final byte zeropage;

  public ZeropageAddress(Ram ram, byte zeropage) {
    this.ram = ram;
    this.zeropage = zeropage;
  }

  @Override
  public String toString() {
    return String.format("$%02x", zeropage);
  }

  @Override
  public Byte fetch() {
    return ram.fetchZeropage(zeropage, Byte.class);
  }

  @Override
  public void store(Byte value) {
    ram.storeZeropage(zeropage, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
