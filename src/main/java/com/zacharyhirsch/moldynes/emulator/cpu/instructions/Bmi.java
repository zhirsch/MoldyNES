package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Bmi extends Bxx {

  public Bmi() {
    super(NesCpuState.testN());
  }
}
