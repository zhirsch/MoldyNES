package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class IndexedAbsoluteAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt16 absolute;
  private final Index index;

  public IndexedAbsoluteAddress(NesCpuMemory memory, UInt16 absolute, Index index) {
    this.memory = memory;
    this.absolute = absolute;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("$%s,%s", absolute, index);
  }

  @Override
  public UInt8 fetch() {
    return memory.fetch(absolute, index.get());
  }

  @Override
  public void store(UInt8 value) {
    memory.store(absolute, index.get(), value);
  }

  @Override
  public UInt8[] getBytes() {
    return new UInt8[] {absolute.lsb(), absolute.msb()};
  }

}
