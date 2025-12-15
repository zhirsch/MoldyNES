package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;

public class Beq extends Bxx {

  public Beq() {
    super(NesCpuStatusRegister::z);
  }
}
