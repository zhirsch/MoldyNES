package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;

public class Nop extends Instruction {

  private final Argument argument;

  public Nop(Argument argument) {
    this.argument = argument;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {}

  @Override
  public Argument getArgument() {
    return argument;
  }
}
