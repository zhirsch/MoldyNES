package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuOam {

  private final byte[] oamBuffer;
  private final byte[] secondaryOamBuffer;
  private final int[] indices;

  private int oamN;
  private int secondaryOamN;
  private int m;
  private byte buffer;
  private boolean copying;

  private int address;

  NesPpuOam() {
    this.oamBuffer = new byte[4 * 64];
    this.secondaryOamBuffer = new byte[4 * 8];
    this.indices = new int[8];
    this.oamN = 0;
    this.secondaryOamN = 0;
    this.m = 0;
    this.buffer = 0;
    this.copying = false;
    this.address = 0;
  }

  void setAddress(byte address) {
    this.address = Byte.toUnsignedInt(address);
  }

  byte get() {
    return oamBuffer[address];
  }

  void put(byte data) {
    oamBuffer[address] = data;
    address++;
    if (address == 0x100) {
      address = 0;
    }
  }

  NesPpuSprite sprite(int index) {
    return new NesPpuSprite(
        indices[index],
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 0]),
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 1]),
        secondaryOamBuffer[4 * index + 2],
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 3]));
  }

  void resetSecondaryOam(int dot) {
    if (dot == 1) {
      reset();
    }
    copy(dot, true);
  }

  void evaluateSprite(int scanline, int dot, int height) {
    // TODO: overflow
    if (dot == 65) {
      reset();
      copying = false;
    }

    if (copying) {
      copying = !copy(dot, false);
      return;
    }

    if (secondaryOamN == 8) {
      return;
    }

    if (oamN == 64) {
      return;
    }

    switch (dot % 2) {
      case 1 -> buffer = oamBuffer[oamN * 4 + m];
      case 0 -> {
        if (isSpriteOnScanline(scanline, Byte.toUnsignedInt(buffer), height)) {
          secondaryOamBuffer[secondaryOamN * 4 + m] = buffer;
          indices[secondaryOamN] = oamN;
          m++;
          copying = true;
        } else {
          oamN++;
        }
      }
    }
  }

  private void reset() {
    oamN = 0;
    secondaryOamN = 0;
    m = 0;
  }

  private boolean copy(int dot, boolean clearing) {
    if (dot % 2 == 1) {
      buffer = oamBuffer[oamN * 4 + m];
      return false;
    }
    secondaryOamBuffer[secondaryOamN * 4 + m] = clearing ? (byte) 0xff : buffer;
    m++;
    if (m == 4) {
      oamN++;
      secondaryOamN++;
      m = 0;
      return true;
    }
    return false;
  }

  private boolean isSpriteOnScanline(int scanline, int y, int height) {
    return y <= scanline && scanline < y + height;
  }
}
