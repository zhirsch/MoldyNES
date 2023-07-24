package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Byte.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public abstract class AdcSbc implements Instruction {

  private final ReadableAddress<Byte> address;

  protected AdcSbc(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // NES doesn't support BCD
    //    if (regs.sr.d) {
    //      executeBcd(regs);
    //    } else {
    executeBinary(regs);
    //    }
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }

  private void executeBinary(Registers regs) {
    byte input = address.fetch();
    int carry = regs.sr.c ? 1 : 0;

    int sum;
    byte unsignedInput;
    if (this instanceof Adc) {
      sum = regs.a + input + carry;
      unsignedInput = input;
    } else {
      sum = regs.a - input - (1 - carry);
      unsignedInput = (byte) ~input;
    }

    regs.sr.n = (byte) sum < 0;
    regs.sr.z = (byte) sum == 0;
    regs.sr.c = toUnsignedInt(regs.a) + toUnsignedInt(unsignedInput) + carry > 255;
    regs.sr.v = sum > 127 || sum < -128;

    regs.a = (byte) sum;
  }

  private void executeBcd(Registers regs) {
    byte input = address.fetch();
    int carry = regs.sr.c ? 1 : 0;

    int sum;
    if (this instanceof Adc) {
      sum = fromBcd(regs.a) + fromBcd(input) + carry;
    } else {
      sum = 100 + (fromBcd(regs.a) - fromBcd(input) - (1 - carry));
    }

    byte sum2 = (byte) (sum % 100);

    regs.sr.n = sum2 < 0;
    regs.sr.z = sum2 == 0;
    regs.sr.c = sum > 99;
    regs.sr.v = sum > 127 || sum < -128;

    regs.a = toBcd(sum2);
  }

  private byte fromBcd(byte value) {
    byte msb = (byte) ((value & 0xf0) >>> 4);
    byte lsb = (byte) (value & 0x0f);
    return (byte) (msb * 10 + lsb);
  }

  private byte toBcd(byte value) {
    byte msb = (byte) (value / 10);
    byte lsb = (byte) (value % 10);
    return (byte) (msb << 4 | lsb);
  }

  public static final class Adc extends AdcSbc {

    public Adc(ReadableAddress<Byte> address) {
      super(address);
    }
  }

  public static final class Sbc extends AdcSbc {

    public Sbc(ReadableAddress<Byte> address) {
      super(address);
    }
  }
}
