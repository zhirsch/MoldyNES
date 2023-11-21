package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public interface FinishFunction {

  void finish(NesCpuState state);
}
