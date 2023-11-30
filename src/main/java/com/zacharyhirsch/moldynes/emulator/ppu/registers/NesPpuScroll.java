package com.zacharyhirsch.moldynes.emulator.ppu.registers;

public final class NesPpuScroll {

  private byte x = 0;
  private byte y = 0;
  private boolean latch = false;

  public byte getX() {
    return x;
  }

  public byte getY() {
    return y;
  }

  public void update(byte data) {
    if (!latch) {
      x = data;
    } else {
      y = data;
    }
    latch = !latch;
  }

  public void reset() {
    latch = false;
  }
}
