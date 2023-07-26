package com.zacharyhirsch.moldynes.emulator;

public class StatusRegister {

  public boolean n = false;
  public boolean v = false;
  public boolean d = false;
  public boolean i = true;
  public boolean z = false;
  public boolean c = false;

  public UInt8 toByte() {
    byte b = 0b0011_0000; // `unused` and `break` are always set
    b |= n ? 0b1000_0000 : 0;
    b |= v ? 0b0100_0000 : 0;
    b |= d ? 0b0000_1000 : 0;
    b |= i ? 0b0000_0100 : 0;
    b |= z ? 0b0000_0010 : 0;
    b |= c ? 0b0000_0001 : 0;
    return UInt8.cast(b);
  }

  public void fromByte(UInt8 sr) {
    n = sr.bit(7) == 1;
    v = sr.bit(6) == 1;
    d = sr.bit(3) == 1;
    i = sr.bit(2) == 1;
    z = sr.bit(1) == 1;
    c = sr.bit(0) == 1;
  }

  @Override
  public String toString() {
    return ""
        + (n ? "N" : "n")
        + (v ? "V" : "v")
        + "-"
        + "b"
        + (d ? "D" : "d")
        + (i ? "I" : "i")
        + (z ? "Z" : "z")
        + (c ? "C" : "c");
  }
}
