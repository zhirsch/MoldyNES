package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

final class AbsoluteXDecompiler implements Decompiler {

  private final String name;

  AbsoluteXDecompiler(String name) {
    this(name, false);
  }

  AbsoluteXDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory) {
    byte bal = fetchByte(memory, pc++);
    byte bah = fetchByte(memory, pc);
    short base = (short) ((bah << 8) | Byte.toUnsignedInt(bal));
    short address = (short) (base + Byte.toUnsignedInt(cpu.state.x));
    byte value = fetchByte(memory, address);
    return String.format(
        "%02X %02X %02X %s $%02X%02X,X @ %04X = %02X",
        opcode, bal, bah, name, bah, bal, address, value);
  }

  private byte fetchByte(NesCpuMemoryMap memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesCpuMemoryMap memory, byte adh, byte adl) {
    return memory.fetch(adh, adl);
  }
}
