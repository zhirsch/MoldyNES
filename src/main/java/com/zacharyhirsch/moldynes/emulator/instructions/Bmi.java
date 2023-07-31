package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public final class Bmi extends BranchInstruction {

  public Bmi(UInt8 opcode) {
    super(opcode, sr -> sr.n);
  }
}
