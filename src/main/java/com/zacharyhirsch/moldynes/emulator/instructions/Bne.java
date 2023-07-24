package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Short.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Immediate;

public class Bne implements Instruction {

  private final Registers regs;
  private final Immediate<Byte> immediate;

  public Bne(Registers regs, Immediate<Byte> immediate) {
    this.regs = regs;
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("BNE $%04X", toUnsignedInt((short) (regs.pc + immediate.fetch())));
    //    return getClass().getSimpleName().toUpperCase() + " " + immediate.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (!regs.sr.z) {
      regs.pc += immediate.fetch();
    }
  }

  @Override
  public int getSize() {
    return 2;
  }
}
