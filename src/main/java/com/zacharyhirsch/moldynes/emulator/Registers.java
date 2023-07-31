package com.zacharyhirsch.moldynes.emulator;

public final class Registers {

  public final ProgramCounter pc;
  public UInt8 a = UInt8.cast(0);
  public UInt8 x = UInt8.cast(0);
  public UInt8 y = UInt8.cast(0);
  public final StatusRegister p;
  public UInt8 sp = UInt8.cast(0xfd);

  public Registers(ProgramCounter pc, StatusRegister p) {
    this.pc = pc;
    this.p = p;
  }

  @Override
  public String toString() {
    return String.format("A:%s X:%s Y:%s P:%s SP:%s", a, x, y, p, sp);
  }

  public Registers copy() {
    Registers copy = new Registers(pc.copy(), p.copy());
    copy.a = a;
    copy.x = x;
    copy.y = y;
    copy.sp = sp;
    return copy;
  }
}
