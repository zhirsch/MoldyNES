package com.zacharyhirsch.moldynes.emulator;

public final class StackPointer {

  private static final int STACK_BASE = 0x0100;

  private UInt8 sp;

  public StackPointer() {
    this(UInt8.cast(0xfd));
  }

  public StackPointer(UInt8 sp) {
    this.sp = sp;
  }

  @Override
  public String toString() {
    return sp.toString();
  }

  public UInt8 value() {
    return sp;
  }

  public void set(UInt8 sp) {
    this.sp = sp;
  }

  public UInt16 address() {
    return UInt16.cast(STACK_BASE + Byte.toUnsignedInt(sp.value()));
  }

  public UInt16 getAddressAndDecrement() {
    UInt16 address = address();
    sp = UInt8.cast(Byte.toUnsignedInt(sp.value()) - 1);
    return address;
  }

  public UInt16 incrementAndGetAddress() {
    sp = UInt8.cast(Byte.toUnsignedInt(sp.value()) + 1);
    return address();
  }

  public StackPointer copy() {
    return new StackPointer(sp);
  }
}
