package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class IndirectAddress implements ReadableAddress<UInt16> {

  private final NesCpuMemory memory;
  private final UInt16 absolute;

  public IndirectAddress(NesCpuMemory memory, UInt16 absolute) {
    this.memory = memory;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("($%s)", absolute);
  }

  @Override
  public UInt16 fetch() {
    UInt8 ial = absolute.lsb();
    UInt8 iah = absolute.msb();

    UInt8 lsb = memory.fetch(absolute);
    UInt8 msb = memory.fetch(new UInt16(iah, NesAlu.add(ial, UInt8.cast(1)).output()));
    return new UInt16(msb, lsb);
  }

  @Override
  public UInt8[] bytes() {
    return new UInt8[] {absolute.lsb(), absolute.msb()};
  }

}
