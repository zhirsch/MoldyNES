package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;

public final class Bvs extends Bxx {

  public Bvs() {
    super(NesCpuStatusRegister::v);
  }
}
