package com.zacharyhirsch.moldynes.emulator.ppu;

record NesPpuSprite(int index, int y, int tileIndex, int attributes, int x) {

  public boolean valid() {
    return y() != 0xff && x() != 0xff;
  }
}
