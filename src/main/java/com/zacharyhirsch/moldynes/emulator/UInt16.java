package com.zacharyhirsch.moldynes.emulator;

public record UInt16(short value) implements Comparable<UInt16> {

  public UInt16(UInt8 lsb, UInt8 msb) {
    this(ByteUtil.compose(lsb.value(), msb.value()));
  }

  public static UInt16 cast(int value) {
    return new UInt16((short) value);
  }

  public static UInt16 cast(UInt8 lsb) {
    return new UInt16(lsb, UInt8.cast(0x00));
  }

  @Override
  public String toString() {
    return String.format("%04x", value());
  }

  @Override
  public int compareTo(UInt16 o) {
    return Short.compareUnsigned(value, o.value);
  }

  public UInt8 lsb() {
    return UInt8.cast(value());
  }

  public UInt8 msb() {
    return UInt8.cast(value() >>> 8);
  }

  public UInt16 add(UInt8 rhs) {
    NesAlu.Result r = NesAlu.add(lsb(), rhs, false);
    UInt8 lsb = r.output();
    UInt8 msb = NesAlu.add(msb(), UInt8.cast(0), r.c()).output();
    return new UInt16(lsb, msb);
  }

  public UInt16 add(UInt16 rhs) {
    NesAlu.Result r = NesAlu.add(lsb(), rhs.lsb(), false);
    UInt8 lsb = r.output();
    UInt8 msb = NesAlu.add(msb(), rhs.msb(), r.c()).output();
    return new UInt16(lsb, msb);
  }

  public UInt16 sub(UInt8 rhs) {
    NesAlu.Result r = NesAlu.sub(lsb(), rhs, false);
    UInt8 lsb = r.output();
    UInt8 msb = NesAlu.sub(msb(), UInt8.cast(0), r.c()).output();
    return new UInt16(lsb, msb);
    // return UInt16.cast(Short.toUnsignedInt(value()) - Short.toUnsignedInt(rhs.value()));
  }

  public UInt16 sub(UInt16 rhs) {
    NesAlu.Result r = NesAlu.sub(lsb(), rhs.lsb(), false);
    UInt8 lsb = r.output();
    UInt8 msb = NesAlu.sub(msb(), rhs.msb(), r.c()).output();
    return new UInt16(lsb, msb);
    // return UInt16.cast(Short.toUnsignedInt(value()) - Short.toUnsignedInt(rhs.value()));
  }
}
