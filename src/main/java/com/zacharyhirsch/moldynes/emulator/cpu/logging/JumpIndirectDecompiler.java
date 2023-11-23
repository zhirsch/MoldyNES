package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class JumpIndirectDecompiler implements Decompiler {

  private final String name;

  JumpIndirectDecompiler(String name) {
    this(name, false);
  }

  private JumpIndirectDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte ial = fetchByte(memory, pc++);
    byte iah = fetchByte(memory, pc++);
    byte adl = fetchByte(memory, iah, ial);
    byte adh = fetchByte(memory, iah, (byte) (ial + 1));
    return String.format(
        "%02X %02X %02X %s ($%02X%02X) = %02X%02X", opcode, ial, iah, name, iah, ial, adh, adl);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) (address >>> 0));
  }

  private static byte fetchByte(NesCpuMemoryMap memory, byte adh, byte adl) {
    return memory.fetch(adh, adl);
  }
}
