package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;

public class Brk extends Instruction {

  private static final UInt16 NMI_VECTOR = UInt16.cast(0xfffe);

  private final ImmediateByte ignore;

  public Brk(ImmediateByte ignore) {
    this.ignore = ignore;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    stack.pushByte(regs.pc.address().msb());
    stack.pushByte(regs.pc.address().lsb());
    stack.pushByte(regs.sr.toByte());
    regs.sr.i = true;
    regs.pc.set(new IndirectAddress(memory, NMI_VECTOR).fetch());
  }

  @Override
  public Argument getArgument() {
    return ignore;
  }
}
