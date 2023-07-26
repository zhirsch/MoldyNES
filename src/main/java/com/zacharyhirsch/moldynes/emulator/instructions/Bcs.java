package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public class Bcs implements Instruction {

  private final Registers regs;
  private final ImmediateByte immediate;

  public Bcs(Registers regs, ImmediateByte immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BCS $%s", regs.pc.offset(immediate.fetch()));
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (regs.sr.c) {
      regs.pc = regs.pc.offset(immediate.fetch());
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
