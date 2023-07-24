package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Short.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;

public final class Bvs implements Instruction {

  private final Registers regs;
  private final Immediate<Byte> immediate;

  public Bvs(Registers regs, Immediate<Byte> immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BVS $%04X", (short) (regs.pc + immediate.fetch()));
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (regs.sr.v) {
      regs.pc += immediate.fetch();
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
