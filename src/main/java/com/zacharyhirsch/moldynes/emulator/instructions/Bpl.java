package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Bpl extends BranchInstruction {

  public Bpl(UInt8 opcode) {
    super(opcode, sr -> !sr.n);
  }
}
