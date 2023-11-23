package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class IndirectXDecompiler implements Decompiler {

  private final String name;

  IndirectXDecompiler(String name) {
    this(name, false);
  }

  IndirectXDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte bal = fetchByte(memory, pc);
    byte balX = (byte) (bal + cpu.state.x);
    byte adl = fetchByte(memory, (byte) 0x00, balX);
    byte adh = fetchByte(memory, (byte) 0x00, (byte) (balX + 1));
    byte value = fetchByte(memory, adh, adl);
    return String.format(
        "%02X %02X    %s ($%02X,X) @ %02X = %02X%02X = %02X",
        opcode, bal, name, bal, balX, adh, adl, value);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesCpuMemoryMap memory, byte adh, byte adl) {
    return memory.fetch(adh, adl);
  }
}