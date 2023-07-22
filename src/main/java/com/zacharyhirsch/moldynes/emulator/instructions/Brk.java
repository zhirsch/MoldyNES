package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.IndirectAddress;

public class Brk implements Instruction {

  private static final short NMI_VECTOR = (short) 0xfffe;

  private final IndirectAddress address;

  public Brk(Ram ram) {
    address = new IndirectAddress(ram, NMI_VECTOR);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase();
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    stack.pushShort((short) (regs.pc + 1));
    stack.push(regs.sr.toByte());
    regs.sr.i = true;
    regs.pc = address.fetch();
  }
}
