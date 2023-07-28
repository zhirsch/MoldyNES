package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;

public class Undocumented extends Instruction {

  private final Instruction instruction;

  public Undocumented(Instruction instruction) {
        this.instruction = instruction;
  }

  @Override
  public String toString() {
    return instruction.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    instruction.execute(memory, stack, regs);
  }

  @Override
  public Argument getArgument() {
    return instruction.getArgument();
  }
}
