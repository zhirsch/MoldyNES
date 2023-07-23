package com.zacharyhirsch.moldynes.emulator;

public final class Registers {

  public short pc;
  public byte ac = 0;
  public byte x = 0;
  public byte y = 0;
  public final StatusRegister sr = new StatusRegister();
  public byte sp = 0;

  public Registers(short pc) {
    this.pc = pc;
  }

  @Override
  public String toString() {
    return String.format(
        """
            pc = %04x
            ac = %02x
            x  = %02x
            y  = %02x
            sr = %s
            sp = %02x
            """,
        pc, ac, x, y, sr, sp);
  }
}
