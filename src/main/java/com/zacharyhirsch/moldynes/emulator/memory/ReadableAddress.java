package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;

public interface ReadableAddress<T> extends Instruction.Argument {
  T fetch();
}
