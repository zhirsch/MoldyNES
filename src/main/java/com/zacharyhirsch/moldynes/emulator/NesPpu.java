package com.zacharyhirsch.moldynes.emulator;

public final class NesPpu implements NesDevice {

  @Override
  public byte fetch(short address) {
    return (byte) 0xcd;
  }

  @Override
  public void store(short address, byte data) {}
}
