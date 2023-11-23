package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class ZeropageXDecompiler implements Decompiler {

  private final String name;

  ZeropageXDecompiler(String name) {
    this(name, false);
  }

  ZeropageXDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte bal = fetchByte(memory, pc);
    byte adl = (byte) (bal + cpu.state.x);
    byte value = fetchByte(memory, (byte) 0x00, adl);
    return String.format(
        "%02X %02X    %s $%02X,X @ %02X = %02X", opcode, bal, name, bal, adl, value);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesCpuMemoryMap memory, byte adh, byte adl) {
    return memory.fetch(adh, adl);
  }
}
