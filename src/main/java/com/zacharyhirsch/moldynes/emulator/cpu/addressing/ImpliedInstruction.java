package com.zacharyhirsch.moldynes.emulator.cpu.addressing;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;

public interface ImpliedInstruction {

  void execute(NesCpu cpu);
}
