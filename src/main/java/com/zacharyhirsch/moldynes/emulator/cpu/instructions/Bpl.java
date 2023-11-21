package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Bpl extends Bxx {

  public Bpl() {
    super(NesCpuState.testN().negate());
  }
}
