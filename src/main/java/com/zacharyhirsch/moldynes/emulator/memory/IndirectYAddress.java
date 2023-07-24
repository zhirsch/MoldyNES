package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndirectYAddress implements Address<Byte> {

  private final NesCpuMemory memory;
  private final byte zeropage;
  private final YIndex index;

  public IndirectYAddress(NesCpuMemory memory, byte zeropage, YIndex index) {
    this.memory = memory;
    this.zeropage = zeropage;
    this.index = index;
  }

  @Override
  public String toString() {
    return String.format("($%02X),Y", zeropage);
  }

  @Override
  public Byte fetch() {
    short base = memory.fetchZeropageShort(zeropage);
    return memory.fetchByte(base, index.get());
  }

  @Override
  public void store(Byte value) {
    short base = memory.fetchZeropageShort(zeropage);
    memory.store(base, index, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
