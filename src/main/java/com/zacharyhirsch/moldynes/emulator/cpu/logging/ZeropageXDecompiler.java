package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class ZeropageXDecompiler implements Decompiler {

  private final String name;

  ZeropageXDecompiler(String name) {
    this(name, false);
  }

  ZeropageXDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    byte bal = fetchByte(memory, pc);
    byte adl = (byte) (bal + state.x);
    byte value = fetchByte(memory, (byte) 0x00, adl);
    return String.format(
        "%02X %02X    %s $%02X,X @ %02X = %02X", opcode, bal, name, bal, adl, value);
  }

  private byte fetchByte(NesMemory memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesMemory memory, byte adh, byte adl) {
    return memory.fetchDebug(adh, adl);
  }
}
