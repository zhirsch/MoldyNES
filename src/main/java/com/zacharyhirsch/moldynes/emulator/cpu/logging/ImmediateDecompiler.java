package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class ImmediateDecompiler implements Decompiler {

  private final String name;

  ImmediateDecompiler(String name) {
    this(name, false);
  }

  ImmediateDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte byte1 = fetchByte(memory, pc);
    return String.format("%02X %02X    %s #$%02X", opcode, byte1, name, byte1);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return memory.fetch((byte) (address >>> 8), (byte) (address));
  }
}
