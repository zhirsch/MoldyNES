package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Bne extends Bxx {

  public Bne() {
    super(NesCpuState.testZ().negate());
  }
}
