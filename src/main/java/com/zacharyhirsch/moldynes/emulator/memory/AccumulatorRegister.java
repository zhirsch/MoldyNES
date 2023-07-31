package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class AccumulatorRegister implements Address<UInt8> {

  private final Registers regs;

  public AccumulatorRegister(Registers regs) {
    this.regs = regs;
  }

  @Override
  public String toString() {
    return "A";
  }

  @Override
  public UInt8 fetch() {
    return regs.a;
  }

  @Override
  public void store(UInt8 value) {
    regs.a = value;
  }

  @Override
  public UInt8[] bytes() {
    return new UInt8[0];
  }

}
