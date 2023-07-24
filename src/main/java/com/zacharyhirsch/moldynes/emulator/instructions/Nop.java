package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Argument;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public class Nop implements Instruction {

  private final Argument argument;

  public Nop(Argument argument) {
    this.argument = argument;
  }

  @Override
  public String toString() {
    return "NOP";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {}

  @Override
  public int getSize() {
    return 1 + argument.getSize();
  }
}
