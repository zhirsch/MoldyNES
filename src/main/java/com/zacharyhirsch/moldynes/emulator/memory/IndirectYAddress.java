package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class IndirectYAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt8 zeropage;
  private final YIndex index;

  public IndirectYAddress(NesCpuMemory memory, UInt8 zeropage, YIndex index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%s),Y", zeropage);
  }

  @Override
  public UInt8 fetch() {
    UInt16 base = memory.fetchZeropageWord(zeropage);
    return memory.fetchByte(base, index.get());
  }

  @Override
  public void store(UInt8 value) {
    UInt16 base = memory.fetchZeropageWord(zeropage);
    memory.storeByte(base, index.get(), value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
