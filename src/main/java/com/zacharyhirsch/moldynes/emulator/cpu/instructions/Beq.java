package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Beq extends Bxx {

  public Beq() {
    super(NesCpuState.testZ());
  }
}
