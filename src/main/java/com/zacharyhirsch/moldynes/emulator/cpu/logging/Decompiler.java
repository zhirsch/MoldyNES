package com.zacharyhirsch.moldynes.emulator.cpu.logging;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemoryMap;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

interface Decompiler {

  String decompile(byte opcode, short pc, NesCpu cpu, NesCpuMemoryMap memory);
}
