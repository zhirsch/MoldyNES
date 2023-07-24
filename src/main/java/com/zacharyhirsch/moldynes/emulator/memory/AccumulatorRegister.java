package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Registers;

public class AccumulatorRegister implements Address<Byte> {

  private final Registers regs;

  public AccumulatorRegister(Registers regs) {
    this.regs = regs;
  }

  @Override
  public String toString() {
    return "A";
  }

  @Override
  public Byte fetch() {
    return regs.a;
  }

  @Override
  public void store(Byte value) {
    regs.a = value;
  }

  @Override
  public int getSize() {
    return 0;
  }
}
