package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class IndirectYDecompiler implements Decompiler {

  private final String name;

  IndirectYDecompiler(String name) {
    this(name, false);
  }

  IndirectYDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    byte ial = fetchByte(memory, pc);
    byte bal = fetchByte(memory, (byte) 0x00, ial);
    byte bah = fetchByte(memory, (byte) 0x00, (byte) (ial + 1));
    short base = (short) ((bah << 8) | Byte.toUnsignedInt(bal));
    short address = (short) (base + Byte.toUnsignedInt(state.y));
    byte value = fetchByte(memory, address);
    return String.format(
        "%02X %02X    %s ($%02X),Y = %04X @ %04X = %02X",
        opcode, ial, name, ial, base, address, value);
  }

  private byte fetchByte(NesMemory memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private byte fetchByte(NesMemory memory, byte adh, byte adl) {
    return memory.fetchDebug(adh, adl);
  }
}
