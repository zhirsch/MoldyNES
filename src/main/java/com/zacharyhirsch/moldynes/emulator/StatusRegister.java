package com.zacharyhirsch.moldynes.emulator;

public class StatusRegister {

  public boolean n = false;
  public boolean v = false;
  public boolean d = false;
  public boolean i = true;
  public boolean z = false;
  public boolean c = false;

  public byte toByte() {
    byte b = 0b0011_0000; // `unused` and `break` are always set
    b |= n ? 0b1000_0000 : 0;
    b |= v ? 0b0100_0000 : 0;
    b |= d ? 0b0000_1000 : 0;
    b |= i ? 0b0000_0100 : 0;
    b |= z ? 0b0000_0010 : 0;
    b |= c ? 0b0000_0001 : 0;
    return b;
  }

  public void fromByte(byte sr) {
    n = (sr & 0b1000_0000) != 0;
    v = (sr & 0b0100_0000) != 0;
    d = (sr & 0b0000_1000) != 0;
    i = (sr & 0b0000_0100) != 0;
    z = (sr & 0b0000_0010) != 0;
    c = (sr & 0b0000_0001) != 0;
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
