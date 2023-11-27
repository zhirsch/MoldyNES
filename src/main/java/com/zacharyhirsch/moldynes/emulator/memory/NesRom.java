package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesDevice;

public final class NesRom implements NesDevice {

  private final byte[] rom;

  public NesRom(byte[] rom) {
    this.rom = rom;
  }

  @Override
  public byte fetch(short address) {
    return rom[Short.toUnsignedInt(address)];
  }

  @Override
  public void store(short address, byte data) {
    throw new UnsupportedOperationException();
  }
}
