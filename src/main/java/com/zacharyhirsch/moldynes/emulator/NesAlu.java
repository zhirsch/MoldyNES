package com.zacharyhirsch.moldynes.emulator;

public final class NesAlu {

  private NesAlu() {}

  public record Result(UInt8 output, boolean n, boolean z, boolean c, boolean v) {}

  public static Result add(UInt8 lhs, UInt8 rhs, boolean c) {
    int carry = c ? 1 : 0;
    int output = lhs.value() + rhs.value() + carry;
    return new Result(
        UInt8.cast(output),
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs.value()) + Byte.toUnsignedInt(rhs.value()) + carry > 255,
        output > 127 || output < -128);
  }

  public static Result sub(UInt8 lhs, UInt8 rhs, boolean c) {
    // return add(lhs, ~rhs, !c);
    int carry = c ? 1 : 0;
    int output = lhs.value() - rhs.value() - (1 - carry);
    return new Result(
        UInt8.cast(output),
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs.value()) + Byte.toUnsignedInt((byte) ~rhs.value()) + carry > 255,
        output > 127 || output < -128);
  }

  public static Result inc(UInt8 input) {
    return NesAlu.add(input, UInt8.cast(1), false);
  }

  public static Result dec(UInt8 input) {
    return NesAlu.sub(input, UInt8.cast(1), false);
  }

  public static Result and(UInt8 lhs, UInt8 rhs) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(lhs.value()) & Byte.toUnsignedInt(rhs.value()));
    return new Result(output, output.bit(7) == 1, output.isZero(), false, false);
  }

  public static Result xor(UInt8 lhs, UInt8 rhs) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(lhs.value()) ^ Byte.toUnsignedInt(rhs.value()));
    return new Result(output, output.bit(7) == 1, output.isZero(), false, false);
  }

  public static Result asl(UInt8 input) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(input.value()) << 1);
    return new Result(output, output.bit(7) == 1, output.isZero(), input.bit(7) == 1, false);
  }
}
