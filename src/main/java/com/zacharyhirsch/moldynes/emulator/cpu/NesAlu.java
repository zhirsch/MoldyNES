package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class NesAlu {

  public NesAlu() {}

  public record Result(UInt8 output, boolean n, boolean z, boolean c, boolean v) {}

  public Result add(UInt8 lhs, UInt8 rhs) {
    return add(lhs, rhs, false);
  }

  public Result add(UInt8 lhs, UInt8 rhs, boolean c) {
    int carry = c ? 1 : 0;
    int output = lhs.value() + rhs.value() + carry;
    return new Result(
        UInt8.cast(output),
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs.value()) + Byte.toUnsignedInt(rhs.value()) + carry > 255,
        output > 127 || output < -128);
  }

  public Result sub(UInt8 lhs, UInt8 rhs) {
    return sub(lhs, rhs, true);
  }

  public Result sub(UInt8 lhs, UInt8 rhs, boolean c) {
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

  public Result inc(UInt8 input) {
    return NesAlu.add(input, UInt8.cast(1));
  }

  public Result dec(UInt8 input) {
    return NesAlu.sub(input, UInt8.cast(1));
  }

  public Result and(UInt8 lhs, UInt8 rhs) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(lhs.value()) & Byte.toUnsignedInt(rhs.value()));
    return new Result(output, output.bit(7) == 1, output.isZero(), false, false);
  }

  public Result or(UInt8 lhs, UInt8 rhs) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(lhs.value()) | Byte.toUnsignedInt(rhs.value()));
    return new Result(output, output.bit(7) == 1, output.isZero(), false, false);
  }

  public Result xor(UInt8 lhs, UInt8 rhs) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(lhs.value()) ^ Byte.toUnsignedInt(rhs.value()));
    return new Result(output, output.bit(7) == 1, output.isZero(), false, false);
  }

  public Result asl(UInt8 input) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(input.value()) << 1);
    return new Result(output, output.bit(7) == 1, output.isZero(), input.bit(7) == 1, false);
  }

  public Result rol(UInt8 input, boolean carry) {
    UInt8 output = UInt8.cast((Byte.toUnsignedInt(input.value()) << 1) | (carry ? 1 : 0));
    return new Result(output, output.bit(7) == 1, output.isZero(), input.bit(7) == 1, false);
  }

  public Result lsr(UInt8 input) {
    UInt8 output = UInt8.cast(Byte.toUnsignedInt(input.value()) >> 1);
    return new Result(output, false, output.isZero(), input.bit(0) == 1, false);
  }

  public Result ror(UInt8 input, boolean carry) {
    UInt8 output = UInt8.cast((Byte.toUnsignedInt(input.value()) >> 1) | (carry ? 0b1000_0000 : 0));
    return new Result(output, output.bit(7) == 1, output.isZero(), input.bit(0) == 1, false);
  }
}
