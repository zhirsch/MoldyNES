package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;

public final class Implicit implements Instruction.Argument {
  @Override
  public int getSize() {
    return 0;
  }
}
