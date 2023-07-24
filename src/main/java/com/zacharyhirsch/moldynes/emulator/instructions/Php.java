package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Php implements Instruction {

  public Php(Implicit ignored) {}

  @Override
  public String toString() {
    return "PHP";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.push(regs.sr.toByte(), Byte.class);
  }

  @Override
  public int getSize() {
    return 1;
  }
}
