package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Bvs extends BranchInstruction {

  public Bvs(UInt8 opcode) {
    super(opcode, sr -> sr.v);
  }
}
