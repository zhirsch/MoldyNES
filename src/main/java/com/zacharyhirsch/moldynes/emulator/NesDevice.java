package com.zacharyhirsch.moldynes.emulator;

public interface NesDevice {

  byte fetch(short address);

  void store(short address, byte data);
}
