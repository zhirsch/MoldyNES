package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Bcc extends BranchInstruction {

  public Bcc(UInt8 opcode) {
    super(opcode, sr -> !sr.c);
  }
}
