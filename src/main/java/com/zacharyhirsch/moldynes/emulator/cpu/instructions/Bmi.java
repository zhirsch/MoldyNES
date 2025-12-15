package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;

public final class Bmi extends Bxx {

  public Bmi() {
    super(NesCpuStatusRegister::n);
  }
}
