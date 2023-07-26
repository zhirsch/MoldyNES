package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class ImmediateByte implements ReadableAddress<UInt8> {

  private final UInt8 immediate;

  public ImmediateByte(UInt8 immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("#$%s", immediate);
  }

  @Override
  public UInt8 fetch() {
    return immediate;
  }

  @Override
  public int getSize() {
    return 1;
  }
}
