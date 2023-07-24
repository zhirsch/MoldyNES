package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;

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
        toUnsignedInt(lhs) + toUnsignedInt(rhs) + carry > 255,
        output > 127 || output < -128);
  }

  public static Result sub(byte lhs, byte rhs, boolean c) {
    // return add(lhs, ~rhs, !c);
    int carry = c ? 1 : 0;
    int output = lhs - rhs - (1 - carry);
    return new Result(
        (byte) output,
        (byte) output < 0,
        (byte) output == 0,
        toUnsignedInt(lhs) + toUnsignedInt((byte) ~rhs) + carry > 255,
        output > 127 || output < -128);
  }
}
