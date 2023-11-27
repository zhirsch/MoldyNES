package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;

interface Decompiler {

  String decompile(byte opcode, short pc, NesCpuState state, NesMemory memory);
}
