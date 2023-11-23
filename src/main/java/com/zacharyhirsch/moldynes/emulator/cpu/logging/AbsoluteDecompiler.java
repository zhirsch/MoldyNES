package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class AbsoluteDecompiler implements Decompiler {

  private final String name;

  AbsoluteDecompiler(String name) {
    this(name, false);
  }

  AbsoluteDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte adl = fetchByte(memory, pc++);
    byte adh = fetchByte(memory, pc);
    byte value = fetchByte(memory, adh, adl);
    return String.format(
        "%02X %02X %02X %s $%02X%02X = %02X", opcode, adl, adh, name, adh, adl, value);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesCpuMemoryMap memory, byte adh, byte adl) {
    return memory.fetch(adh, adl);
  }
}
