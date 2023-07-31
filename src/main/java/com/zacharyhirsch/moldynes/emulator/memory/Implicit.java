package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;

public final class Implicit implements Instruction.Argument {

  public Implicit() {}

  @Override
  public String toString() {
    return "";
  }

  @Override
  public UInt8[] bytes() {
    return new UInt8[] {};
  }
}
