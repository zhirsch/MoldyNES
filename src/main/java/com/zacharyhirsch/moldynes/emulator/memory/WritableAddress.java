package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;

public interface WritableAddress<T> extends Instruction.Argument {
  void store(T value);
}