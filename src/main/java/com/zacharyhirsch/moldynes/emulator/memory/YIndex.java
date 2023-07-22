package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Registers;

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
  public byte get() {
    return regs.y;
  }
}
