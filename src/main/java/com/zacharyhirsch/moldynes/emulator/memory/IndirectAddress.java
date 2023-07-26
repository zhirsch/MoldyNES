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

    UInt8 lsb = memory.fetchByte(absolute);
    UInt8 rhs = UInt8.cast(1);
    UInt8 msb = memory.fetchByte(new UInt16(NesAlu.add(ial, rhs, false).output(), iah));
    return new UInt16(lsb, msb);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
