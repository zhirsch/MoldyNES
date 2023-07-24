package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndexedAbsoluteAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final short absolute;
  private final Index index;

  public IndexedAbsoluteAddress(NesCpuMemory memory, short absolute, Index index) {
    this.memory = memory;
    this.absolute = absolute;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%04X,%s", absolute, index);
  }

  @Override
  public Byte fetch() {
    return memory.fetchByte(absolute, index.get());
  }

  @Override
  public void store(Byte value) {
    memory.store(absolute, index, value);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
