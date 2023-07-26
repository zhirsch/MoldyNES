package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public class Bcc implements Instruction {

  private final Registers regs;
  private final ImmediateByte immediate;

  public Bcc(Registers regs, ImmediateByte immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BCC $%s", regs.pc.add(immediate.fetch()));
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (!regs.sr.c) {
      regs.pc = regs.pc.add(immediate.fetch());
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
