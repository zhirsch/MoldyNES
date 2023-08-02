package com.zacharyhirsch.moldynes.emulator;

public final class NesApu implements NesDevice {

  @Override
  public UInt8 readRegister(UInt16 address) {
    return UInt8.cast(0xff);
  }

  @Override
  public void writeRegister(UInt16 address, UInt8 value) {}
}
