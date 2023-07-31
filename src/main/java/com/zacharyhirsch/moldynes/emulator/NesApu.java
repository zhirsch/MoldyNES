package com.zacharyhirsch.moldynes.emulator;

public final class NesApu implements NesDevice {

  @Override
  public UInt8 readRegister(UInt16 address) {
    return null;
  }

  @Override
  public void writeRegister(UInt16 address, UInt8 value) {}
}
