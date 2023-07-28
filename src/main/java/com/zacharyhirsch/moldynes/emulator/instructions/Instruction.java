package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public abstract class Instruction {

  public abstract void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs);

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + getArgument();
  }

  public abstract Argument getArgument();

  public interface Argument {

    UInt8[] getBytes();
  }
}
