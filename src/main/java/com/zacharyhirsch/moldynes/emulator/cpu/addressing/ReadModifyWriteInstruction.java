package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

public interface ReadModifyWriteInstruction {

  byte execute(NesCpu cpu, byte value);
}
