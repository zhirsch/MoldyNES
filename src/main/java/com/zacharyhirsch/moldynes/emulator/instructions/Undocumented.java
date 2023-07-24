package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;

public class Undocumented implements Instruction {

  private final Instruction instr;

  public Undocumented(Instruction instr) {
    this.instr = instr;
  }

  @Override
  public String toString() {
    return instr.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    instr.execute(memory, stack, regs);
  }

  @Override
  public int getSize() {
    return instr.getSize();
  }
}
