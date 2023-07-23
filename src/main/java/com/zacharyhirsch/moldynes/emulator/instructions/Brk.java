package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
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
  public void execute(Ram ram, Registers regs) {
    ram.push((short) (regs.pc + 1), Short.class);
    ram.push(regs.sr.toByte(), Byte.class);
    regs.sr.i = true;
    regs.pc = new IndirectAddress(ram, NMI_VECTOR).fetch();
  }

  @Override
  public int getSize() {
    return 1;
  }
}
