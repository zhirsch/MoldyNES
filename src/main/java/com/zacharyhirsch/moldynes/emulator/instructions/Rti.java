package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.ProgramCounter;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Rti implements Instruction {

  public Rti(Implicit ignored) {}

  @Override
  public String toString() {
    return "RTI";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    regs.sr.fromByte(stack.pullByte());
    regs.pc = new ProgramCounter(stack.pullWord());
  }

  @Override
  public int getSize() {
    return 1;
  }
}
