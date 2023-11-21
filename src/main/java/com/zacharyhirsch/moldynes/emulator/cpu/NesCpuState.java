package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAlu;
import java.util.function.Predicate;

public final class NesCpuState {

  private static final byte STATUS_N = (byte) 0b1000_0000;
  private static final byte STATUS_V = (byte) 0b0100_0000;
  private static final byte STATUS_B = (byte) 0b0001_0000;
  private static final byte STATUS_D = (byte) 0b0000_1000;
  private static final byte STATUS_I = (byte) 0b0000_0100;
  private static final byte STATUS_Z = (byte) 0b0000_0010;
  private static final byte STATUS_C = (byte) 0b0000_0001;

  public byte a = 0;
  public byte x = 0;
  public byte y = 0;

  public byte p = 0b0010_0000 | STATUS_I;

  public byte sp = 0;

  public short pc;

  public byte pch() {
    return (byte) (pc >>> 8);
  }

  public byte pcl() {
    return (byte) pc;
  }

  public byte adh;
  public byte adl;
  public byte data;

  public byte hold;

  public boolean write = false;
  public NesAlu alu = null;

  public void pN(boolean set) {
    p = (byte) (set ? (p | STATUS_N) : (p & (byte) ~STATUS_N));
  }

  public void pV(boolean set) {
    p = (byte) (set ? (p | STATUS_V) : (p & (byte) ~STATUS_V));
  }

  public void pD(boolean set) {
    p = (byte) (set ? (p | STATUS_D) : (p & (byte) ~STATUS_D));
  }

  public void pI(boolean set) {
    p = (byte) (set ? (p | STATUS_I) : (p & (byte) ~STATUS_I));
  }

  public void pZ(boolean set) {
    p = (byte) (set ? (p | STATUS_Z) : (p & (byte) ~STATUS_Z));
  }

  public void pC(boolean set) {
    p = (byte) (set ? (p | STATUS_C) : (p & (byte) ~STATUS_C));
  }

  public boolean pC() {
    return testC().test(p);
  }

  public static Predicate<Byte> testN() {
    return p -> (p & STATUS_N) == STATUS_N;
  }

  public static Predicate<Byte> testV() {
    return p -> (p & STATUS_V) == STATUS_V;
  }

  public static Predicate<Byte> testZ() {
    return p -> (p & STATUS_Z) == STATUS_Z;
  }

  public static Predicate<Byte> testC() {
    return p -> (p & STATUS_C) == STATUS_C;
  }
}
