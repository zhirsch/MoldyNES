package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;

public class Bcs extends Bxx {

  public Bcs() {
    super(NesCpuStatusRegister::c);
  }
}
