package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Bvc extends BranchInstruction {

  public Bvc(UInt8 opcode) {
    super(opcode, sr -> !sr.v);
  }
}
