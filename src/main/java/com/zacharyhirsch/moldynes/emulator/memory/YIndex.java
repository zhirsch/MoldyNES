package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class YIndex implements Index {

  private final Registers regs;

  public YIndex(Registers regs) {
    this.regs = regs;
  }

  @Override
  public String toString() {
    return "Y";
  }

  @Override
  public UInt8 get() {
    return regs.y;
  }
}
