package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Implicit;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;

public class Brk implements Instruction {

  private static final short NMI_VECTOR = (short) 0xfffe;

  public Brk(Implicit ignored) {}

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.push((short) (regs.pc + 1), Short.class);
    stack.push(regs.sr.toByte(), Byte.class);
    regs.sr.i = true;
    regs.pc = new IndirectAddress(memory, NMI_VECTOR).fetch();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
