package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;

public class Bcs implements Instruction {

  private final Registers regs;
  private final Immediate<Byte> immediate;

  public Bcs(Registers regs, Immediate<Byte> immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BCS $%04X", (short) (regs.pc + immediate.fetch()));
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (regs.sr.c) {
      regs.pc += immediate.fetch();
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
