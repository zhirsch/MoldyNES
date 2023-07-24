package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.ByteUtil;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;

public class IndirectAddress implements ReadableAddress<Short> {

  private final NesCpuMemory memory;
  private final short absolute;

  public IndirectAddress(NesCpuMemory memory, short absolute) {
    this.memory = memory;
    this.absolute = absolute;
  }

  @Override
  public String toString() {
    return String.format("($%04X)", absolute);
  }

  @Override
  public Short fetch() {
    byte ial = (byte) absolute;
    byte iah = (byte) (absolute >>> 8);

    byte lsb = memory.fetchByte(ByteUtil.compose(ial, iah));
    byte msb = memory.fetchByte(ByteUtil.compose((byte) (ial + 1), iah));
    return ByteUtil.compose(lsb, msb);
  }

  @Override
  public int getSize() {
    return 2;
  }
}
