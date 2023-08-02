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

  public UInt16 getAddressAndIncrement() {
    UInt16 address = pc;
    pc = UInt16.cast(Short.toUnsignedInt(pc.value()) + 1);
    return address;
  }

  public UInt16 address() {
    return pc;
  }

  public void set(UInt16 address) {
    this.pc = address;
  }

  public ProgramCounter copy() {
    return new ProgramCounter(pc);
  }
}
