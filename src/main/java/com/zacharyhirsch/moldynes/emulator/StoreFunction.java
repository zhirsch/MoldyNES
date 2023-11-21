package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public interface StoreFunction {

  byte value(NesCpuState state);
}
