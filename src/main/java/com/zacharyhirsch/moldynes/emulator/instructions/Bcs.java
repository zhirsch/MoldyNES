package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Bcs extends BranchInstruction {

  public Bcs(UInt8 opcode) {
    super(opcode, sr -> sr.c);
  }
}
