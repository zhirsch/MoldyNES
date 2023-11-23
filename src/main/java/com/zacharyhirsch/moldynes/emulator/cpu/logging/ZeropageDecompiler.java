package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class ZeropageDecompiler implements Decompiler {

  private final String name;

  ZeropageDecompiler(String name) {
    this(name, false);
  }

  ZeropageDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte adl = fetchByte(memory, pc++);
    byte value = fetchByte(memory, adl);
    return String.format("%02X %02X    %s $%02X = %02X", opcode, adl, name, adl, value);
  }

  private byte fetchByte(NesCpuMemoryMap memory, byte adl) {
    short address = (short) Byte.toUnsignedInt(adl);
    return fetchByte(memory, address);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return memory.fetch((byte) (address >>> 8), (byte) (address >>> 0));
  }
}
