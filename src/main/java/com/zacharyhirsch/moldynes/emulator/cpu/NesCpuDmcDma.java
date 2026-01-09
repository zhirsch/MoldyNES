package com.zacharyhirsch.moldynes.emulator.cpu;

import java.util.function.Consumer;

class NesCpuDmcDma implements NesCpuCycle {

  private final short address;
  private final Consumer<Byte> callback;
  private final NesCpuCycle resume;

  NesCpuDmcDma(short address, Consumer<Byte> callback, NesCpuCycle resume) {
    this.address = address;
    this.callback = callback;
    this.resume = resume;
  }

  @Override
  public NesCpuCycle execute(NesCpu cpu) {
    return cycle1(cpu);
  }

  private NesCpuCycle cycle1(NesCpu cpu) {
    // Halt cycle.
    return this::cycle2;
  }

  private NesCpuCycle cycle2(NesCpu cpu) {
    // Dummy cycle, do nothing.
    return this::cycle3;
  }

  private NesCpuCycle cycle3(NesCpu cpu) {
    if (cpu.state.cycleType != NesCpuState.CycleType.GET) {
      // Alignment cycle
      return this::cycle3;
    }
    cpu.fetch(address);
    return this::cycle4;
  }

  private NesCpuCycle cycle4(NesCpu cpu) {
    callback.accept(cpu.state.data);
    return resume;
  }
}
