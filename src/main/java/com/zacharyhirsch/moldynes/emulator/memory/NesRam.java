package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.NesDevice;
import java.nio.ByteBuffer;

public final class NesRam implements NesDevice {

  private final ByteBuffer ram;

  public NesRam(ByteBuffer ram) {
    this.ram = ram;
  }

  @Override
  public byte fetch(short address) {
    return ram.get(Short.toUnsignedInt(address));
  }

  @Override
  public void store(short address, byte data) {
    ram.put(Short.toUnsignedInt(address), data);
  }
}
