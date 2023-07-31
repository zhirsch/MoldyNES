package com.zacharyhirsch.moldynes.emulator;

public final class ProgramCounter {

  private UInt16 pc;

  public ProgramCounter(UInt16 pc) {
    this.pc = pc;
  }

  @Override
  public String toString() {
    return pc.toString();
  }

  public UInt16 address() {
    return pc;
  }

  public void set(UInt16 pc) {
    this.pc = pc;
  }

  public void inc() {
    pc = UInt16.cast(Short.toUnsignedInt(pc.value()) + 1);
  }

  public void dec() {
    pc = UInt16.cast(Short.toUnsignedInt(pc.value()) - 1);
  }

  public ProgramCounter copy() {
    return new ProgramCounter(pc);
  }
}
