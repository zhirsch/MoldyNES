package com.zacharyhirsch.moldynes.emulator;

public interface NesDevice {

  UInt8 readRegister(UInt16 address);

  void writeRegister(UInt16 address, UInt8 value);
}
