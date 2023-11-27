package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class ImpliedDecompiler implements Decompiler {

  private final String name;

  ImpliedDecompiler(String name) {
    this(name, false);
  }

  ImpliedDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    return String.format("%02X       %s", opcode, name);
  }
}
