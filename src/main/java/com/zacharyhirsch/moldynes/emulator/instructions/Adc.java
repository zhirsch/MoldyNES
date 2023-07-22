package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Byte.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Adc implements Instruction {

  private final ReadableAddress<Byte> address;

  public Adc(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    byte value = address.fetch();
    byte carry = (byte) (regs.sr.c ? 1 : 0);

    int sum = regs.ac + value + carry;
    regs.ac = (byte) sum;

    regs.sr.n = regs.ac < 0;
    regs.sr.z = regs.ac == 0;
    regs.sr.c = toUnsignedInt(regs.ac) + toUnsignedInt(value) + toUnsignedInt(carry) > 255;
    regs.sr.v = sum > 127 || sum < -128;
  }
}
