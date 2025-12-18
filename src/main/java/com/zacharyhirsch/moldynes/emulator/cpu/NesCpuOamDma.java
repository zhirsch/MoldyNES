package com.zacharyhirsch.moldynes.emulator.cpu;

class NesCpuOamDma implements NesCpuCycle {

  private final byte address;
  private final NesCpuCycle resume;

  NesCpuOamDma(byte address, NesCpuCycle resume) {
    this.address = address;
    this.resume = resume;
  }

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    if (cpu.state.cycleType != NesCpuState.CycleType.GET) {
      // alignment cycle
      cpu.fetch(address, (byte) 0);
      return this;
    }
    return cycle1(cpu, 0);
  }

  private NesCpuCycle cycle1(NesCpu cpu, int index) {
    cpu.fetch(address, (byte) index);
    return c -> cycle2(c, index);
  }

  private NesCpuCycle cycle2(NesCpu cpu, int index) {
    cpu.store((short) 0x2004, cpu.state.data);
    if (index == 0xff) {
      return resume;
    }
    return c -> cycle1(c, index + 1);
  }
}
