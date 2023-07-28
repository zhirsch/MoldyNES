package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public final class Bmi extends BranchInstruction {

  public Bmi(Registers regs, ImmediateByte immediate) {
    super(regs, immediate, sr -> sr.n);
  }
}
