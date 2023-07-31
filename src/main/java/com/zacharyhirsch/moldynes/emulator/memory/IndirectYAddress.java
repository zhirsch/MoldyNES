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
    UInt8 bal = memory.fetch(zeropage);
    UInt8 bah = memory.fetch(UInt8.cast(zeropage.value() + 1));
    return memory.fetch(new UInt16(bah, bal), index.get());
  }

  @Override
  public void store(UInt8 value) {
    UInt8 bal = memory.fetch(zeropage);
    UInt8 bah = memory.fetch(UInt8.cast(zeropage.value() + 1));
    memory.store(new UInt16(bah, bal), index.get(), value);
  }

  @Override
  public UInt8[] bytes() {
    return new UInt8[] {zeropage};
  }

}
