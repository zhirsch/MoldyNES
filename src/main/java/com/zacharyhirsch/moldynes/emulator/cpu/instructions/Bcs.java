package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public class Bcs extends Bxx {

  public Bcs() {
    super(NesCpuState.testC());
  }
}
