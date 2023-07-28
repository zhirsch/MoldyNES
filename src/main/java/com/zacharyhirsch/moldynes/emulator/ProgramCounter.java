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

  public void offset(UInt8 offset) {
    pc = UInt16.cast(pc.value() + offset.value());
  }

  public void inc() {
    offset(UInt8.cast(1));
  }

  public void dec() {
    offset(UInt8.cast(-1));
  }
}
