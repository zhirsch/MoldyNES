package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;

public class Iny implements Instruction {

  public Iny(Implicit ignored) {}

  @Override
  public String toString() {
    return "INY";
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.inc(regs.y);
    regs.y = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
