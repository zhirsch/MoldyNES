package com.zacharyhirsch.moldynes.emulator;

public final class Registers {

  public ProgramCounter pc;
  public UInt8 a = UInt8.cast(0);
  public UInt8 x = UInt8.cast(0);
  public UInt8 y = UInt8.cast(0);
  public final StatusRegister sr = new StatusRegister();
  public UInt8 sp = UInt8.cast(0xfd);

  public Registers(ProgramCounter pc) {
    this.pc = pc;
  }

  @Override
  public String toString() {
    return String.format(
        """
            pc = %s
            a  = %s
            x  = %s
            y  = %s
            sr = %s
            sp = %s
            """,
        pc, a, x, y, sr, sp);
  }
}
