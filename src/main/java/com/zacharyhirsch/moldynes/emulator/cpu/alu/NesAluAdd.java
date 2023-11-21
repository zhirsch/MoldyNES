package com.zacharyhirsch.moldynes.emulator.cpu.alu;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class NesAluAdd implements NesAlu {

  private final byte lhs;
  private final byte rhs;
  private final boolean carry;

  private byte output;
  private boolean n, z, c, v;

  public NesAluAdd(byte lhs, byte rhs, boolean carry) {
    this.lhs = lhs;
    this.rhs = rhs;
    this.carry = carry;
  }

  @Override
  public void execute() {
    int carry = this.carry ? 1 : 0;
    output = (byte) (lhs + rhs + carry);
    n = output < 0;
    z = output == 0;
    c = Byte.toUnsignedInt(lhs) + Byte.toUnsignedInt(rhs) + carry > 255;
    v = (lhs + rhs + carry) > 127 || (lhs + rhs + carry) < -128;
  }

  @Override
  public byte output() {
    return output;
  }

  @Override
  public boolean n() {
    return n;
  }

  @Override
  public boolean z() {
    return z;
  }

  @Override
  public boolean c() {
    return c;
  }

  @Override
  public boolean v() {
    return v;
  }
}
