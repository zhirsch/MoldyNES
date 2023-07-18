package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

import java.util.Arrays;

public abstract class Adc implements Instruction {

  public static class Immediate implements Instruction {

    private final byte immediate;

    public Immediate(byte immediate) {
      this.immediate = immediate;
    }

    @Override
    public String describe() {
      return String.format("ADC #$%02x", immediate);
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
      byte carry = (byte) (regs.sr.c ? 1 : 0);
      int value = regs.ac + immediate + carry;
      regs.ac = (byte) value;

      regs.sr.n = regs.ac < 0;
      regs.sr.z = regs.ac == 0;
      regs.sr.c = binaryAdd(regs.ac, immediate, carry) > 255;
      regs.sr.v = value > 127 || value < -128;
    }

    private static int binaryAdd(Byte... values) {
      return Arrays.stream(values).mapToInt(Byte::toUnsignedInt).sum();
    }
  }
}
