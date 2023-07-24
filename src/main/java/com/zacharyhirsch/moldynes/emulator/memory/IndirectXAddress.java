package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndirectXAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final byte zeropage;
  private final XIndex index;

  public IndirectXAddress(NesCpuMemory memory, byte zeropage, XIndex index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%02X,X) @ 00 = 0000 = 00", zeropage);
  }

  @Override
  public Byte fetch() {
    short address = memory.fetchZeropage(zeropage, index, Short.class);
    return memory.fetch(address, Byte.class);
  }

  @Override
  public void store(Byte value) {
    short address = memory.fetchZeropage(zeropage, index, Short.class);
    memory.store(address, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
