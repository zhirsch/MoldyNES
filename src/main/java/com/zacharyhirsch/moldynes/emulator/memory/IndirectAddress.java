package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndirectAddress implements ReadableAddress<Short> {

  private final NesCpuMemory memory;
  private final short absolute;

  public IndirectAddress(NesCpuMemory memory, short absolute) {
    this.memory = memory;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("($%04x)", absolute);
  }

  @Override
  public Short fetch() {
    return memory.fetch(absolute, Short.class);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
