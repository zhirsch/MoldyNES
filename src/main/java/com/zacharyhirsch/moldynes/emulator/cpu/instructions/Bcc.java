package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Bcc extends Bxx {

  public Bcc() {
    super(NesCpuState.testC().negate());
  }
}
