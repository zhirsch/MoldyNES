package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class IndirectXAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt8 zeropage;
  private final XIndex index;

  public IndirectXAddress(NesCpuMemory memory, UInt8 zeropage, XIndex index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%s,X)", zeropage);
  }

  @Override
  public UInt8 fetch() {
    UInt16 addr = memory.fetchZeropageWord(zeropage, index.get());
    return memory.fetchByte(addr);
  }

  @Override
  public void store(UInt8 value) {
    UInt16 address = memory.fetchZeropageWord(zeropage, index.get());
    memory.storeByte(address, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
