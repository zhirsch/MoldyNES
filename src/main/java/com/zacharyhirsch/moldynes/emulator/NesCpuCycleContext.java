package com.zacharyhirsch.moldynes.emulator;

public final class NesCpuCycleContext {

  private final NesCpuMemory memory;
  private final NesCpuRegisters registers;

  private int cycles;

  public NesCpuCycleContext(NesCpuMemory memory, NesCpuRegisters registers) {
    this.memory = memory;
    this.registers = registers;
    this.cycles = 0;
  }

  public UInt8 fetch(UInt8 zeropage) {
    cycles++;
    return memory.fetch(zeropage);
  }

  public UInt8 fetch(UInt16 address) {
    cycles++;
    return memory.fetch(address);
  }

  public void store(UInt8 address, UInt8 value) {
    cycles++;
    memory.store(address, value);
  }

  public void store(UInt16 address, UInt8 value) {
    cycles++;
    memory.store(address, value);
  }

  public NesCpuMemory memory() {
    return memory;
  }

  public NesCpuRegisters registers() {
    return registers;
  }

  public int cycles() {
    return cycles;
  }
}
