package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public interface ModifyFunction {

  void modify(NesCpuState state);
}
