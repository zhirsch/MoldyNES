package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Bvc extends Bxx {

  public Bvc() {
    super(NesCpuState.testV().negate());
  }
}
