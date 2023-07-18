package com.zacharyhirsch.moldynes.emulator;

import java.util.BitSet;

public class Registers {

  public static final int STATUS_REGISTER_N = 7;
  public static final int STATUS_REGISTER_V = 6;
  public static final int STATUS_REGISTER_B = 4;
  public static final int STATUS_REGISTER_D = 3;
  public static final int STATUS_REGISTER_I = 2;
  public static final int STATUS_REGISTER_Z = 1;
  public static final int STATUS_REGISTER_C = 0;

  public short pc;
  public byte ac;
  public byte x;
  public byte y;
  public final BitSet sr;
  public byte sp;

  public Registers(short pc) {
    this.pc = pc;
    this.ac = 0;
    this.x = 0;
    this.y = 0;
    this.sr = new BitSet(8);
    this.sp = 0;
  }
}
