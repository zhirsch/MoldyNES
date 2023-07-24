package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class ZeropageAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final byte zeropage;

  public ZeropageAddress(NesCpuMemory memory, byte zeropage) {
    this.memory = memory;
    this.zeropage = zeropage;
  }

  @Override
  public String toString() {
    return String.format("$%02X = 00", zeropage);
  }

  @Override
  public Byte fetch() {
    return memory.fetchZeropage(zeropage, Byte.class);
  }

  @Override
  public void store(Byte value) {
    memory.storeZeropage(zeropage, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
