package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Registers;

public class XIndex implements Index {

  private final Registers regs;

  public XIndex(Registers regs) {
    this.regs = regs;
  }

  @Override
  public String toString() {
    return "X";
  }

  @Override
  public byte get() {
    return regs.x;
  }
}
