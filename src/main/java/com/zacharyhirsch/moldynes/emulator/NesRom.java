package com.zacharyhirsch.moldynes.emulator;

import java.nio.ByteBuffer;

public final class NesRom implements NesDevice {

  private final ByteBuffer rom;

  public NesRom(ByteBuffer rom) {
    this.rom = rom.asReadOnlyBuffer();
  }

  @Override
  public byte fetch(short address) {
    return rom.get(Short.toUnsignedInt(address));
  }

  @Override
  public void store(short address, byte data) {
    throw new UnsupportedOperationException();
  }
}
