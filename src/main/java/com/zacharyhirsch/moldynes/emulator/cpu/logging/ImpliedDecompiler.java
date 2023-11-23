package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class ImpliedDecompiler implements Decompiler {

  private final String name;

  ImpliedDecompiler(String name) {
    this(name, false);
  }

  private ImpliedDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    return String.format("%02X       %s", opcode, name);
  }
}
