package com.zacharyhirsch.moldynes.emulator.ppu;

final class NesPpuOam {

  private final byte[] oamBuffer;
  private final byte[] secondaryOamBuffer;

  private int oamN;
  private int secondaryOamN;
  private byte buffer;
  private boolean selected;

  private byte address;

  NesPpuOam() {
    this.oamBuffer = new byte[4 * 64];
    this.secondaryOamBuffer = new byte[4 * 8];
    this.oamN = 0;
    this.secondaryOamN = 0;
    this.buffer = 0;
    this.selected = false;
    this.address = 0;
  }

  void setAddress(byte address) {
    this.address = address;
  }

  byte get() {
    return oamBuffer[Byte.toUnsignedInt(address)];
  }

  void put(byte data) {
    oamBuffer[Byte.toUnsignedInt(address++)] = data;
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
    }
    switch (dot % 8) {
      // y
      case 1 -> buffer = oamBuffer[oamN * 4 + 0];
      case 2 -> {
        if (secondaryOamN != 8) {
          secondaryOamBuffer[secondaryOamN * 4 + 0] = buffer;
        }
        selected = secondaryOamN != 8 && isSpriteOnScanline(scanline, buffer);
      }
      // tile index
      case 3 -> buffer = oamBuffer[oamN * 4 + 1];
      case 4 -> {
        if (selected) {
          secondaryOamBuffer[secondaryOamN * 4 + 1] = buffer;
        }
      }
      // attributes
      case 5 -> buffer = oamBuffer[oamN * 4 + 2];
      case 6 -> {
        if (selected) {
          secondaryOamBuffer[secondaryOamN * 4 + 2] = buffer;
        }
      }
      // x
      case 7 -> {
        buffer = oamBuffer[oamN * 4 + 3];
        oamN++;
      }
      case 0 -> {
        if (selected) {
          secondaryOamBuffer[secondaryOamN * 4 + 3] = buffer;
          secondaryOamN++;
        }
      }
    }
  }

  private boolean isSpriteOnScanline(int scanline, byte y) {
    return y <= scanline && scanline < y + 8;
  }
}
