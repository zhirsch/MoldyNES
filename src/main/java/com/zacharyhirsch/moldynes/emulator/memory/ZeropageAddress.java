package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class ZeropageAddress implements Address<UInt8> {

  private final NesCpuMemory memory;
  private final UInt8 zeropage;

  public ZeropageAddress(NesCpuMemory memory, UInt8 zeropage) {
    this.memory = memory;
    this.zeropage = zeropage;
  }

  @Override
  public String toString() {
    return String.format("$%s", zeropage);
  }

  @Override
  public UInt8 fetch() {
    return memory.fetchZeropageByte(zeropage);
  }

  @Override
  public void store(UInt8 value) {
    memory.storeZeropageByte(zeropage, value);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
