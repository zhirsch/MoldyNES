package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class AbsoluteAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt16 absolute;

  public AbsoluteAddress(NesCpuMemory memory, UInt16 absolute) {
    this.memory = memory;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("$%s", absolute);
  }

  @Override
  public UInt8 fetch() {
    return memory.fetchByte(absolute);
  }

  @Override
  public void store(UInt8 value) {
    memory.storeByte(absolute, value);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
