package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.UInt16;

public class ImmediateWord implements ReadableAddress<UInt16> {

  private final UInt16 immediate;

  public ImmediateWord(UInt16 immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("#$%s", immediate);
  }

  @Override
  public UInt16 fetch() {
    return immediate;
  }

  @Override
  public int getSize() {
    return 2;
  }
}
