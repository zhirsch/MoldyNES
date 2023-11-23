package com.zacharyhirsch.moldynes.emulator.cpu;

public final class NesAlu {

  public NesAlu() {}

  public record Result(byte output, boolean n, boolean z, boolean c, boolean v) {}

  public Result add(byte lhs, byte rhs) {
    return add(lhs, rhs, false);
  }

  public Result add(byte lhs, byte rhs, boolean c) {
    int carry = c ? 1 : 0;
    int output = lhs + rhs + carry;
    return new Result(
        (byte) output,
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs) + Byte.toUnsignedInt(rhs) + carry > 255,
        output > 127 || output < -128);
  }

  public Result sub(byte lhs, byte rhs) {
    return sub(lhs, rhs, true);
  }

  public Result sub(byte lhs, byte rhs, boolean c) {
    int carry = c ? 1 : 0;
    int output = lhs - rhs - (1 - carry);
    return new Result(
        (byte) output,
        (byte) output < 0,
        (byte) output == 0,
        Byte.toUnsignedInt(lhs) + Byte.toUnsignedInt((byte) ~rhs) + carry > 255,
        output > 127 || output < -128);
  }

  public Result inc(byte input) {
    return add(input, (byte) 1);
  }

  public Result dec(byte input) {
    return sub(input, (byte) 1);
  }

  public Result and(byte lhs, byte rhs) {
    byte output = (byte) (Byte.toUnsignedInt(lhs) & Byte.toUnsignedInt(rhs));
    return new Result(output, output < 0, output == 0, false, false);
  }

  public Result or(byte lhs, byte rhs) {
    byte output = (byte) (Byte.toUnsignedInt(lhs) | Byte.toUnsignedInt(rhs));
    return new Result(output, output < 0, output == 0, false, false);
  }

  public Result xor(byte lhs, byte rhs) {
    byte output = (byte) (Byte.toUnsignedInt(lhs) ^ Byte.toUnsignedInt(rhs));
    return new Result(output, output < 0, output == 0, false, false);
  }

  public Result asl(byte input) {
    byte output = (byte) (Byte.toUnsignedInt(input) << 1);
    return new Result(output, output < 0, output == 0, input < 0, false);
  }

  public Result rol(byte input, boolean carry) {
    byte output = (byte) ((Byte.toUnsignedInt(input) << 1) | (carry ? 1 : 0));
    return new Result(output, output < 0, output == 0, input < 0, false);
  }

  public Result lsr(byte input) {
    byte output = (byte) (Byte.toUnsignedInt(input) >> 1);
    return new Result(output, false, output == 0, (input & 1) == 1, false);
  }

  public Result ror(byte input, boolean carry) {
    byte output = (byte) ((Byte.toUnsignedInt(input) >> 1) | (carry ? 0b1000_0000 : 0));
    return new Result(output, output < 0, output == 0, (input & 1) == 1, false);
  }
}
