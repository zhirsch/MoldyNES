package com.zacharyhirsch.moldynes.emulator.rom;

public record NesRomProperties(int mapper, NametableLayout nametableLayout) {

  public enum NametableLayout {
    HORIZONTAL,
    VERTICAL,
    ;

    static NametableLayout fromHeader6(byte header6) {
      return (header6 & 1) == 1 ? VERTICAL : HORIZONTAL;
    }
  }
}
