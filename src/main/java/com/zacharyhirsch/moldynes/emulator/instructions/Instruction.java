package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;

public interface Instruction {

  void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs);

  int getSize();

  interface Argument {

    int getSize();
  }
}
