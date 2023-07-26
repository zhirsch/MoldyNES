package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;

public class Brk implements Instruction {

  private static final UInt16 NMI_VECTOR = UInt16.cast(0xfffe);

  public Brk(Implicit ignored) {}

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.pushWord(regs.pc.inc().address());
    stack.pushByte(regs.sr.toByte());
    regs.sr.i = true;
    regs.pc = new ProgramCounter(new IndirectAddress(memory, NMI_VECTOR).fetch());
  }

  @Override
  public int getSize() {
    return 1;
  }
}
