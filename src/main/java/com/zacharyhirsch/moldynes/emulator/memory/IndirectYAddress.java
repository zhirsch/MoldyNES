package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndirectYAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final byte zeropage;
  private final YIndex index;

  public IndirectYAddress(NesCpuMemory memory, byte zeropage, YIndex index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%02X),Y = 0000 @ 0000 = 00", zeropage);
  }

  @Override
  public Byte fetch() {
    short base = memory.fetchZeropage(zeropage, Short.class);
    return memory.fetch(base, index, Byte.class);
  }

  @Override
  public void store(Byte value) {
    short base = memory.fetchZeropage(zeropage, Short.class);
    memory.store(base, index, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
