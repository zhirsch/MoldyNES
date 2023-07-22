package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public class Cpy implements Instruction {

  private final ReadableAddress<Byte> address;

  public Cpy(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    byte result = (byte) (regs.y - address.fetch());

    regs.sr.n = result < 0;
    regs.sr.z = result == 0;
    regs.sr.c = result >= 0;
  }
}
