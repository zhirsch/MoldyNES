package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Bne extends BranchInstruction {

  public Bne(UInt8 opcode) {
    super(opcode, sr -> !sr.z);
  }
}
