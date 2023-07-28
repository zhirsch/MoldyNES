package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;

public final class Ignore implements Instruction.Argument {

  private final UInt8[] bytes;

  public Ignore(UInt8... bytes) {
    this.bytes = bytes;
  }

  @Override
  public String toString() {
    return "";
  }

  @Override
  public UInt8[] getBytes() {
    return bytes;
  }
}
