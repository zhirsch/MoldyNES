package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

public final class NesCpuCycleContext {

  private final NesRam memory;
  private final NesCpu registers;

  private int cycles;

  public NesCpuCycleContext(NesRam memory, NesCpu registers) {
    this.memory = memory;
    this.registers = registers;
    this.cycles = 0;
  }

  public UInt8 fetch(UInt16 address) {
    cycles++;
    return memory.fetch(address);
  }

  public void store(UInt16 address, UInt8 value) {
    cycles++;
    memory.store(address, value);
  }

  public NesRam memory() {
    return memory;
  }

  public NesCpu registers() {
    return registers;
  }

  public int cycles() {
    return cycles;
  }
}
