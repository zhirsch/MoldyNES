package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Eor implements Instruction {

  private final ReadableAddress<Byte> address;

  public Eor(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    regs.ac = (byte) (regs.ac ^ address.fetch());

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
  }
}
