package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class AbsoluteAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final short absolute;

  public AbsoluteAddress(NesCpuMemory memory, short absolute) {
    this.memory = memory;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("$%04X", absolute);
  }

  @Override
  public Byte fetch() {
      return memory.fetchByte(absolute);
  }

  @Override
  public void store(Byte value) {
    memory.store(absolute, value);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
