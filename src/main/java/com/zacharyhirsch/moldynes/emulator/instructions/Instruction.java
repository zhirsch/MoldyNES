package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;

public interface Instruction {

  void execute(Ram ram, Registers regs);

  int getSize();

  interface Argument {

    int getSize();
  }
}
