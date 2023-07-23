package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Byte.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.Ram;
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
  public void execute(Ram ram, Registers regs) {
    if (regs.sr.d) {
      executeBcd(regs);
    } else {
      executeBinary(regs);
    }
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }

  private interface Function3<V1, V2, V3, R> {
    R apply(V1 v1, V2 v2, V3 v3);
  }

  private void executeBinary(Registers regs) {
    byte input = address.fetch();
    int carry = regs.sr.c ? 1 : 0;

    int sum;
    byte unsignedInput;
    if (this instanceof Adc) {
      sum = regs.ac + input + carry;
      unsignedInput = input;
    } else {
      sum = regs.ac - input - (1 - carry);
      unsignedInput = (byte) ~input;
    }

    regs.sr.n = (byte) sum < 0;
    regs.sr.z = (byte) sum == 0;
    regs.sr.c = toUnsignedInt(regs.ac) + toUnsignedInt(unsignedInput) + carry > 255;
    regs.sr.v = sum > 127 || sum < -128;

    regs.ac = (byte) sum;
  }

  private void executeBcd(Registers regs) {
    byte input = address.fetch();
    int carry = regs.sr.c ? 1 : 0;

    int sum;
    if (this instanceof Adc) {
      sum = fromBcd(regs.ac) + fromBcd(input) + carry;
    } else {
      sum = 100 + (fromBcd(regs.ac) - fromBcd(input) - (1 - carry));
    }

    byte sum2 = (byte) (sum % 100);

    regs.sr.n = sum2 < 0;
    regs.sr.z = sum2 == 0;
    regs.sr.c = sum > 99;
    regs.sr.v = sum > 127 || sum < -128;

    regs.ac = toBcd(sum2);
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
