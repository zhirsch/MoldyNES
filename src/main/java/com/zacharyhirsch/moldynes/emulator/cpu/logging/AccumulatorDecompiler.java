package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class AccumulatorDecompiler implements Decompiler {

  private final String name;

  AccumulatorDecompiler(String name) {
    this(name, false);
  }

  private AccumulatorDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    return String.format("%02X       %s A", opcode, name);
  }
}
