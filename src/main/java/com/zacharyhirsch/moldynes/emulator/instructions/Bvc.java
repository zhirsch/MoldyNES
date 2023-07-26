package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;

public final class Bvc implements Instruction {

  private final Registers regs;
  private final ImmediateByte immediate;

  public Bvc(Registers regs, ImmediateByte immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BVC $%s", regs.pc.add(immediate.fetch()));
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (!regs.sr.v) {
      regs.pc = regs.pc.add(immediate.fetch());
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
