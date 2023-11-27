package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

final class JumpIndirectDecompiler implements Decompiler {

  private final String name;

  JumpIndirectDecompiler(String name) {
    this(name, false);
  }

  private JumpIndirectDecompiler(String name, boolean undocumented) {
    this.name = (undocumented ? "*" : " ") + name;
  }

  @Override
  public String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory) {
    byte ial = fetchByte(memory, pc++);
    byte iah = fetchByte(memory, pc);
    byte adl = fetchByte(memory, iah, ial);
    byte adh = fetchByte(memory, iah, (byte) (ial + 1));
    return String.format(
        "%02X %02X %02X %s ($%02X%02X) = %02X%02X", opcode, ial, iah, name, iah, ial, adh, adl);
  }

  private byte fetchByte(NesMemory memory, short address) {
    return fetchByte(memory, (byte) (address >>> 8), (byte) address);
  }

  private static byte fetchByte(NesMemory memory, byte adh, byte adl) {
    return memory.fetchDebug(adh, adl);
  }
}
