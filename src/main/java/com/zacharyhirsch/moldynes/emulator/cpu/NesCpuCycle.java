package com.zacharyhirsch.moldynes.emulator.cpu;


public interface NesCpuCycle {

  NesCpuCycle start(NesCpu cpu, NesCpuState state);
}
