package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;
import java.nio.ByteBuffer;
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
    public void execute(ByteBuffer ram, Registers regs) {
      byte carry = (byte) (regs.sr.get(Registers.STATUS_REGISTER_C) ? 1 : 0);
      int value = regs.ac + immediate + carry;
      regs.ac = (byte) value;

      regs.sr.set(Registers.STATUS_REGISTER_N, regs.ac < 0);
      regs.sr.set(Registers.STATUS_REGISTER_Z, regs.ac == 0);
      regs.sr.set(Registers.STATUS_REGISTER_C, binaryAdd(regs.ac, immediate, carry) > 255);
      regs.sr.set(Registers.STATUS_REGISTER_V, value > 127 || value < -128);
    }

    private static int binaryAdd(Byte... values) {
      return Arrays.stream(values).mapToInt(Byte::toUnsignedInt).sum();
    }
  }
}
