package com.zacharyhirsch.moldynes.emulator;

public final class ProgramCounter {

  private final UInt16 pc;

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

  public ProgramCounter offset(UInt8 offset) {
    return new ProgramCounter(UInt16.cast(pc.value() + offset.value()));
  }

  public ProgramCounter inc() {
    return offset(UInt8.cast(1));
  }

  public ProgramCounter dec() {
    return offset(UInt8.cast(-1));
  }
}
