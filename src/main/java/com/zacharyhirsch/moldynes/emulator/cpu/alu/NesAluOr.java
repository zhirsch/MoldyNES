package com.zacharyhirsch.moldynes.emulator.cpu.alu;


public final class NesAluOr implements NesAlu {

  private final byte lhs;
  private final byte rhs;

  private byte output;
  private boolean n, z, c, v;

  public NesAluOr(byte lhs, byte rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  @Override
  public void execute() {
    output = (byte) (lhs | rhs);
    n = output < 0;
    z = output == 0;
    c = false;
    v = false;
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
