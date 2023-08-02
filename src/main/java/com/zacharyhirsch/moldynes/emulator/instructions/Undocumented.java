package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;

public class Undocumented extends Instruction {

  private final Instruction instruction;

  public Undocumented(Instruction instruction) {
    this.instruction = instruction;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return instruction.execute(context);
  }
}
