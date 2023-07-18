package com.zacharyhirsch.moldynes.emulator;

public class Registers {

  public short pc;
  public byte ac;
  public byte x;
  public byte y;
  public final StatusRegister sr;
  public byte sp;

  public Registers(short pc) {
    this.pc = pc;
    this.ac = 0;
    this.x = 0;
    this.y = 0;
    this.sr = new StatusRegister();
    this.sp = 0;
  }

  @Override
  public String toString() {
    return String.format(
        "pc = %04x\nac = %02x\nx  = %02x\ny  = %02x\nsr = %s\nsp = %02x", pc, ac, x, y, sr, sp);
  }
}
