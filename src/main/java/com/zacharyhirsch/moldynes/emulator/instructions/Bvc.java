package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public final class Bvc extends BranchInstruction {

  public Bvc(Registers regs, ImmediateByte immediate) {
    super(regs, immediate, sr -> !sr.v);
  }
}
