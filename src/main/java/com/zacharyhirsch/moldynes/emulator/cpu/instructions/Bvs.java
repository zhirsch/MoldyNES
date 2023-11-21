package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Bvs extends Bxx {

  public Bvs() {
    super(NesCpuState.testV());
  }
}
