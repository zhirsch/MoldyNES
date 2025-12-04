package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuOam {

  private final byte[] oamBuffer;
  final byte[] secondaryOamBuffer;
  private final byte[] indices;

  private int oamN;
  private int secondaryOamN;
  private byte buffer;
  private boolean copying;
  private boolean inRange;
  private int m;

  private int address;

  NesPpuOam() {
    this.oamBuffer = new byte[4 * 64];
    this.secondaryOamBuffer = new byte[4 * 8];
    this.indices = new byte[8];
    this.oamN = 0;
    this.secondaryOamN = 0;
    this.buffer = 0;
    this.copying = false;
    this.inRange = false;
    this.m = 0;
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
        Byte.toUnsignedInt(indices[index]),
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 0]),
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 1]),
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 2]),
        Byte.toUnsignedInt(secondaryOamBuffer[4 * index + 3]));
  }

  void tick(int scanline, int dot) {
    assert 0 <= scanline && scanline <= 261;
    assert 0 <= dot && dot <= 340;
    if (0 <= scanline && scanline <= 239) {
      if (1 <= dot && dot <= 64) {
        resetSecondaryOam(dot);
        return;
      }
      if (65 <= dot && dot <= 256) {
        evaluateSprite(scanline, dot);
        return;
      }
      if (257 <= dot && dot <= 320) {
        // TODO: sprite fetch
        return;
      }
      if (321 <= dot && dot <= 340) {
        oamN = 0;
        secondaryOamN = 0;
        return;
      }
    }
  }

  private void resetSecondaryOam(int dot) {
    if (dot == 1) {
      oamN = 0;
      secondaryOamN = 0;
    }
    switch (dot % 8) {
      case 1 -> buffer = oamBuffer[oamN * 4 + 0];
      case 2 -> secondaryOamBuffer[secondaryOamN * 4 + 0] = (byte) 0xff;
      case 3 -> buffer = oamBuffer[oamN * 4 + 1];
      case 4 -> secondaryOamBuffer[secondaryOamN * 4 + 1] = (byte) 0xff;
      case 5 -> buffer = oamBuffer[oamN * 4 + 2];
      case 6 -> secondaryOamBuffer[secondaryOamN * 4 + 2] = (byte) 0xff;
      case 7 -> {
        buffer = oamBuffer[oamN * 4 + 3];
        oamN++;
      }
      case 0 -> {
        secondaryOamBuffer[secondaryOamN * 4 + 3] = (byte) 0xff;
        secondaryOamN++;
      }
    }
  }

  private void evaluateSprite(int scanline, int dot) {
    // TODO: overflow
    if (dot == 65) {
      oamN = 0;
      secondaryOamN = 0;
      copying = false;
      inRange = false;
      m = 0;
    }

    if (!copying && oamN >= 64) {
      return;
    }

    if (secondaryOamN >= 8) {
      return;
    }

    if (copying) {
      switch (dot % 2) {
        case 1 -> buffer = oamBuffer[oamN * 4 + m];
        case 0 -> {
          secondaryOamBuffer[secondaryOamN * 4 + m] = buffer;
          m++;
          if (m >= 4) {
            oamN++;
            secondaryOamN++;
            copying = false;
            inRange = false;
            m = 0;
          }
        }
      }
      return;
    }

    switch (dot % 2) {
      case 1 -> buffer = oamBuffer[oamN * 4 + m];
      case 0 -> {
        inRange = isSpriteOnScanline(scanline, Byte.toUnsignedInt(buffer));
        if (inRange) {
          secondaryOamBuffer[secondaryOamN * 4 + m] = buffer;
          m++;
          copying = true;
        } else {
          oamN++;
        }
      }
    }
  }

  private boolean isSpriteOnScanline(int scanline, int y) {
    return y <= scanline && scanline < y + 8;
  }
}
