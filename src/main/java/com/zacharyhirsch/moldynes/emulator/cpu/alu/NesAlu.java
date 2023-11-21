package com.zacharyhirsch.moldynes.emulator.cpu.alu;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public interface NesAlu {

  void execute();

  byte output();

  boolean n();
  boolean z();
  boolean c();
  boolean v();
}
