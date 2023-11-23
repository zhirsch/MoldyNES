package com.zacharyhirsch.moldynes.emulator.cpu;

public interface NesCpuCycleTemp extends NesCpuCycle {

  default NesCpuCycleTemp execute(NesCpu cpu) { return null; }
}
