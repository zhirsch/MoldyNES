package com.zacharyhirsch.moldynes.emulator.cpu;

public final class NesAlu {

  private NesAlu() {}

  public record Result(byte output, boolean n, boolean z, boolean c, boolean v) {}

  public static Result add(byte lhs, byte rhs, boolean c) {
    int carry = c ? 1 : 0;
    int output = lhs + rhs + carry;
    return new Result(
        (byte) output,
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs) + Byte.toUnsignedInt(rhs) + carry > 255,
        output > 127 || output < -128);
  }

  public static Result sub(byte lhs, byte rhs, boolean c) {
    return add(lhs, (byte) ~rhs, c);
  }

  public static Result cmp(byte lhs, byte rhs) {
    return sub(lhs, rhs, true);
  }
}
