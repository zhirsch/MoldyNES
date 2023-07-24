package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndexedZeropageAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final byte zeropage;
  private final Index index;

  public IndexedZeropageAddress(NesCpuMemory memory, byte zeropage, Index index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%02X,%s", zeropage, index);
  }

  @Override
  public Byte fetch() {
    return memory.fetchZeropageByte(zeropage, index.get());
  }

  @Override
  public void store(Byte value) {
    memory.storeZeropage(zeropage, index, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
