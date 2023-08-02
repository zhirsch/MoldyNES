package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Bmi extends BranchInstruction {

  public Bmi(UInt8 opcode) {
    super(opcode, sr -> sr.n);
  }
}
