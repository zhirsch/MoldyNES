package com.zacharyhirsch.moldynes.emulator.cpu;

import java.util.BitSet;

public final class NesCpuStatusRegister {

  private static final byte STATUS_N = (byte) 0b1000_0000;
  private static final byte STATUS_V = (byte) 0b0100_0000;
  private static final byte STATUS_D = (byte) 0b0000_1000;
  private static final byte STATUS_I = (byte) 0b0000_0100;
  private static final byte STATUS_Z = (byte) 0b0000_0010;
  private static final byte STATUS_C = (byte) 0b0000_0001;

  private byte p;

  NesCpuStatusRegister() {
    this.p = STATUS_I;
  }

  public void fromByte(byte p) {
    this.p = (byte) (p & 0b1100_1111);
  }

  public byte toByte(boolean b) {
    BitSet bitSet = new BitSet(8);
    bitSet.set(0, c());
    bitSet.set(1, z());
    bitSet.set(2, i());
    bitSet.set(3, d());
    bitSet.set(4, b);
    bitSet.set(5, true);
    bitSet.set(6, v());
    bitSet.set(7, n());
    return bitSet.toByteArray()[0];
  }

  public void c(boolean set) {
    p = set(STATUS_C, set);
  }

  public void z(boolean set) {
    p = set(STATUS_Z, set);
  }

  public void i(boolean set) {
    p = set(STATUS_I, set);
  }

  public void d(boolean set) {
    p = set(STATUS_D, set);
  }

  public void v(boolean set) {
    p = set(STATUS_V, set);
  }

  public void n(boolean set) {
    p = set(STATUS_N, set);
  }

  public boolean c() {
    return get(STATUS_C);
  }

  public boolean z() {
    return get(STATUS_Z);
  }

  public boolean i() {
    return get(STATUS_I);
  }

  public boolean d() {
    return get(STATUS_D);
  }

  public boolean v() {
    return get(STATUS_V);
  }

  public boolean n() {
    return get(STATUS_N);
  }

  private byte set(byte bit, boolean set) {
    return (byte) (set ? (p | bit) : (p & (byte) ~bit));
  }

  private boolean get(byte bit) {
    return (p & bit) == bit;
  }
}
