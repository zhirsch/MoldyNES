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
    UInt8 adl = memory.fetchZeropageByte(zeropage, index.get());
    UInt8 adh = memory.fetchZeropageByte(UInt8.cast(zeropage.value() + 1), index.get());
    return memory.fetchByte(new UInt16(adl, adh));
  }

  @Override
  public void store(UInt8 value) {
    UInt8 adl = memory.fetchZeropageByte(zeropage, index.get());
    UInt8 adh = memory.fetchZeropageByte(UInt8.cast(zeropage.value() + 1), index.get());
    memory.storeByte(new UInt16(adl, adh), value);
  }

  @Override
  public UInt8[] getBytes() {
    return new UInt8[] {zeropage};
  }

}
