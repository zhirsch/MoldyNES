package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public class Bcc extends BranchInstruction {

  public Bcc(Registers regs, ImmediateByte immediate) {
    super(regs, immediate, sr -> !sr.c);
  }
}
