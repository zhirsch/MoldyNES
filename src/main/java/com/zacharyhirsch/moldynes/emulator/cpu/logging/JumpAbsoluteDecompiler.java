package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class JumpAbsoluteDecompiler implements Decompiler {

  private final String name;

  JumpAbsoluteDecompiler(String name) {
    this(name, false);
  }

  private JumpAbsoluteDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte adl = fetchByte(memory, pc++);
    byte adh = fetchByte(memory, pc++);
    return String.format("%02X %02X %02X %s $%02X%02X", opcode, adl, adh, name, adh, adl);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return memory.fetch((byte) (address >>> 8), (byte) (address >>> 0));
  }
}
