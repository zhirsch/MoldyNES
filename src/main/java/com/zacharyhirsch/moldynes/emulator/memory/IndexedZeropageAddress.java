package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class IndexedZeropageAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt8 zeropage;
  private final Index index;

  public IndexedZeropageAddress(NesCpuMemory memory, UInt8 zeropage, Index index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%s,%s", zeropage, index);
  }

  @Override
  public UInt8 fetch() {
    return memory.fetchZeropageByte(zeropage, index.get());
  }

  @Override
  public void store(UInt8 value) {
    memory.storeZeropageByte(zeropage, index.get(), value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
