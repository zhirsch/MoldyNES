package com.zacharyhirsch.moldynes.emulator;

public final class NesCpuRegisters {

  public final ProgramCounter pc;
  public UInt8 a = UInt8.cast(0);
  public UInt8 x = UInt8.cast(0);
  public UInt8 y = UInt8.cast(0);
  public final StatusRegister p;
  public final StackPointer sp;

  public NesCpuRegisters(ProgramCounter pc, StatusRegister p, StackPointer sp) {
    this.pc = pc;
    this.p = p;
    this.sp = sp;
  }

  @Override
  public String toString() {
    return String.format("A:%s X:%s Y:%s P:%s SP:%s", a, x, y, p, sp);
  }

  public NesCpuRegisters copy() {
    NesCpuRegisters copy = new NesCpuRegisters(pc.copy(), p.copy(), sp.copy());
    copy.a = a;
    copy.x = x;
    copy.y = y;
    return copy;
  }
}
