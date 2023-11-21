package com.zacharyhirsch.moldynes.emulator;

public final class NesApu implements NesDevice {

  @Override
  public byte fetch(short address) {
    return (byte) 0xff;
  }

  @Override
  public void store(short address, byte data) {}
}
