package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Plp implements Instruction {

  public Plp(Implicit ignored) {}

  @Override
  public String toString() {
    return "PLP";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.sr.fromByte(stack.pull(Byte.class));
  }

  @Override
  public int getSize() {
    return 1;
  }
}
