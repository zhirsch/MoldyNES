package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Beq extends BranchInstruction {

  public Beq(UInt8 opcode) {
    super(opcode, sr -> sr.z);
  }
}
