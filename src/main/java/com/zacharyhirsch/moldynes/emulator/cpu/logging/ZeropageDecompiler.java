package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class ZeropageDecompiler implements Decompiler {

  private final String name;

  ZeropageDecompiler(String name) {
    this(name, false);
  }

  ZeropageDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    byte adl = fetchByte(memory, pc);
    byte value = fetchByte(memory, adl);
    return String.format("%02X %02X    %s $%02X = %02X", opcode, adl, name, adl, value);
  }

  private byte fetchByte(NesMemory memory, byte adl) {
    short address = (short) Byte.toUnsignedInt(adl);
    return fetchByte(memory, address);
  }

  private byte fetchByte(NesMemory memory, short address) {
    return memory.fetchDebug((byte) (address >>> 8), (byte) address);
  }
}
