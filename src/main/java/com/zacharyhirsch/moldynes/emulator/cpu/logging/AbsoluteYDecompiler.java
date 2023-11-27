package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class AbsoluteYDecompiler implements Decompiler {

  private final String name;

  AbsoluteYDecompiler(String name) {
    this(name, false);
  }

  AbsoluteYDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    byte bal = fetchByte(memory, pc++);
    byte bah = fetchByte(memory, pc);
    short base = (short) ((bah << 8) | Byte.toUnsignedInt(bal));
    short address = (short) (base + Byte.toUnsignedInt(state.y));
    byte value = fetchByte(memory, address);
    return String.format(
        "%02X %02X %02X %s $%02X%02X,Y @ %04X = %02X",
        opcode, bal, bah, name, bah, bal, address, value);
  }

  private byte fetchByte(NesMemory memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesMemory memory, byte adh, byte adl) {
    return memory.fetchDebug(adh, adl);
  }
}
