package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.NesDevice;
import java.util.Arrays;

public final class NesPpu implements NesDevice {

  public record Color(byte r, byte g, byte b) {}

  public record Pixel(Color color) {}

  public interface DrawFrame {

    void draw(byte[] frame);
  }

  private static final byte[][] SYSTEM_PALETTE =
      new byte[][] {
        //        new byte[] {(byte) 0x80, (byte) 0x80, (byte) 0x80},
        //        new byte[] {0x00, 0x3D, (byte) 0xA6},
        //        new byte[] {0x00, 0x12, (byte) 0xB0},
        //        new byte[] {0x44, 0x00, (byte) 0x96},
        //        new byte[] {(byte) 0xA1, 0x00, 0x5E},
        //        new byte[] {(byte) 0xC7, 0x00, 0x28},
        //        new byte[] {(byte) 0xBA, 0x06, 0x00},
        //        new byte[] {(byte) 0x8C, 0x17, 0x00},
        //        new byte[] {0x5C, 0x2F, 0x00},
        //        new byte[] {0x10, 0x45, 0x00},
        //        new byte[] {0x05, 0x4A, 0x00},
        //        new byte[] {0x00, 0x47, 0x2E},
        //        new byte[] {0x00, 0x41, 0x66},
        //        new byte[] {0x00, 0x00, 0x00},
        //        new byte[] {0x05, 0x05, 0x05},
        //        new byte[] {0x05, 0x05, 0x05},
        //        new byte[] {(byte) 0xC7, (byte) 0xC7, (byte) 0xC7},
        //        new byte[] {0x00, 0x77, (byte) 0xFF},
        //        new byte[] {0x21, 0x55, (byte) 0xFF},
        //        new byte[] {(byte) 0x82, 0x37, (byte) 0xFA},
        //        new byte[] {(byte) 0xEB, 0x2F, (byte) 0xB5},
        //        new byte[] {(byte) 0xFF, 0x29, 0x50},
        //        new byte[] {(byte) 0xFF, 0x22, 0x00},
        //        new byte[] {(byte) 0xD6, 0x32, 0x00},
        //        new byte[] {(byte) 0xC4, 0x62, 0x00},
        //        new byte[] {0x35, (byte) 0x80, 0x00},
        //        new byte[] {0x05, (byte) 0x8F, 0x00},
        //        new byte[] {0x00, (byte) 0x8A, 0x55},
        //        new byte[] {0x00, (byte) 0x99, (byte) 0xCC},
        //        new byte[] {0x21, 0x21, 0x21},
        //        new byte[] {0x09, 0x09, 0x09},
        //        new byte[] {0x09, 0x09, 0x09},
        //        new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF},
        //        new byte[] {0x0F, (byte) 0xD7, (byte) 0xFF},
        //        new byte[] {0x69, (byte) 0xA2, (byte) 0xFF},
        //        new byte[] {(byte) 0xD4, (byte) 0x80, (byte) 0xFF},
        //        new byte[] {(byte) 0xFF, 0x45, (byte) 0xF3},
        //        new byte[] {(byte) 0xFF, 0x61, (byte) 0x8B},
        //        new byte[] {(byte) 0xFF, (byte) 0x88, 0x33},
        //        new byte[] {(byte) 0xFF, (byte) 0x9C, 0x12},
        //        new byte[] {(byte) 0xFA, (byte) 0xBC, 0x20},
        //        new byte[] {(byte) 0x9F, (byte) 0xE3, 0x0E},
        //        new byte[] {0x2B, (byte) 0xF0, 0x35},
        //        new byte[] {0x0C, (byte) 0xF0, (byte) 0xA4},
        //        new byte[] {0x05, (byte) 0xFB, (byte) 0xFF},
        //        new byte[] {0x5E, 0x5E, 0x5E},
        //        new byte[] {0x0D, 0x0D, 0x0D},
        //        new byte[] {0x0D, 0x0D, 0x0D},
        //        new byte[] {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF},
        //        new byte[] {(byte) 0xA6, (byte) 0xFC, (byte) 0xFF},
        //        new byte[] {(byte) 0xB3, (byte) 0xEC, (byte) 0xFF},
        //        new byte[] {(byte) 0xDA, (byte) 0xAB, (byte) 0xEB},
        //        new byte[] {(byte) 0xFF, (byte) 0xA8, (byte) 0xF9},
        //        new byte[] {(byte) 0xFF, (byte) 0xAB, (byte) 0xB3},
        //        new byte[] {(byte) 0xFF, (byte) 0xD2, (byte) 0xB0},
        //        new byte[] {(byte) 0xFF, (byte) 0xEF, (byte) 0xA6},
        //        new byte[] {(byte) 0xFF, (byte) 0xF7, (byte) 0x9C},
        //        new byte[] {(byte) 0xD7, (byte) 0xE8, (byte) 0x95},
        //        new byte[] {(byte) 0xA6, (byte) 0xED, (byte) 0xAF},
        //        new byte[] {(byte) 0xA2, (byte) 0xF2, (byte) 0xDA},
        //        new byte[] {(byte) 0x99, (byte) 0xFF, (byte) 0xFC},
        //        new byte[] {(byte) 0xDD, (byte) 0xDD, (byte) 0xDD},
        //        new byte[] {0x11, 0x11, 0x11},
        //        new byte[] {0x11, 0x11, 0x11},

        new byte[] {(byte) 0x65, (byte) 0x65, (byte) 0x65},
        new byte[] {(byte) 0x00, (byte) 0x2b, (byte) 0x9b},
        new byte[] {(byte) 0x11, (byte) 0x0e, (byte) 0xc0},
        new byte[] {(byte) 0x3f, (byte) 0x00, (byte) 0xbc},
        new byte[] {(byte) 0x66, (byte) 0x00, (byte) 0x8f},
        new byte[] {(byte) 0x7b, (byte) 0x00, (byte) 0x45},
        new byte[] {(byte) 0x79, (byte) 0x01, (byte) 0x00},
        new byte[] {(byte) 0x60, (byte) 0x1c, (byte) 0x00},
        new byte[] {(byte) 0x36, (byte) 0x38, (byte) 0x00},
        new byte[] {(byte) 0x08, (byte) 0x4f, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x5a, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x57, (byte) 0x02},
        new byte[] {(byte) 0x00, (byte) 0x45, (byte) 0x55},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0xae, (byte) 0xae, (byte) 0xae},
        new byte[] {(byte) 0x07, (byte) 0x61, (byte) 0xf5},
        new byte[] {(byte) 0x3e, (byte) 0x3b, (byte) 0xff},
        new byte[] {(byte) 0x7c, (byte) 0x1d, (byte) 0xff},
        new byte[] {(byte) 0xaf, (byte) 0x0e, (byte) 0xe5},
        new byte[] {(byte) 0xcb, (byte) 0x13, (byte) 0x83},
        new byte[] {(byte) 0xc8, (byte) 0x2a, (byte) 0x15},
        new byte[] {(byte) 0xa7, (byte) 0x4d, (byte) 0x00},
        new byte[] {(byte) 0x6f, (byte) 0x72, (byte) 0x00},
        new byte[] {(byte) 0x37, (byte) 0x91, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x9f, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x9b, (byte) 0x2a},
        new byte[] {(byte) 0x00, (byte) 0x84, (byte) 0x98},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff},
        new byte[] {(byte) 0x56, (byte) 0xb1, (byte) 0xff},
        new byte[] {(byte) 0x8e, (byte) 0x8b, (byte) 0xff},
        new byte[] {(byte) 0xcc, (byte) 0x6c, (byte) 0xff},
        new byte[] {(byte) 0xff, (byte) 0x5d, (byte) 0xff},
        new byte[] {(byte) 0xff, (byte) 0x62, (byte) 0xd4},
        new byte[] {(byte) 0xff, (byte) 0x79, (byte) 0x64},
        new byte[] {(byte) 0xf8, (byte) 0x9d, (byte) 0x06},
        new byte[] {(byte) 0xc0, (byte) 0xc3, (byte) 0x00},
        new byte[] {(byte) 0x81, (byte) 0xe2, (byte) 0x00},
        new byte[] {(byte) 0x4d, (byte) 0xf1, (byte) 0x16},
        new byte[] {(byte) 0x30, (byte) 0xec, (byte) 0x7a},
        new byte[] {(byte) 0x34, (byte) 0xd5, (byte) 0xea},
        new byte[] {(byte) 0x4e, (byte) 0x4e, (byte) 0x4e},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0xff, (byte) 0xff, (byte) 0xff},
        new byte[] {(byte) 0xba, (byte) 0xdf, (byte) 0xff},
        new byte[] {(byte) 0xd1, (byte) 0xd0, (byte) 0xff},
        new byte[] {(byte) 0xeb, (byte) 0xc3, (byte) 0xff},
        new byte[] {(byte) 0xff, (byte) 0xbd, (byte) 0xff},
        new byte[] {(byte) 0xff, (byte) 0xbf, (byte) 0xee},
        new byte[] {(byte) 0xff, (byte) 0xc8, (byte) 0xc0},
        new byte[] {(byte) 0xfc, (byte) 0xd7, (byte) 0x99},
        new byte[] {(byte) 0xef, (byte) 0xe7, (byte) 0x84},
        new byte[] {(byte) 0xcc, (byte) 0xf3, (byte) 0x87},
        new byte[] {(byte) 0xb6, (byte) 0xf9, (byte) 0xa0},
        new byte[] {(byte) 0xaa, (byte) 0xf8, (byte) 0xc9},
        new byte[] {(byte) 0xac, (byte) 0xee, (byte) 0xf7},
        new byte[] {(byte) 0xb7, (byte) 0xb7, (byte) 0xb7},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
        new byte[] {(byte) 0x00, (byte) 0x00, (byte) 0x00},
      };

  private final byte[] chrRom;
  private final byte[] ram;
  private final byte[] oam;
  private final byte[] palette;
  private final DrawFrame drawFrame;

  private int scanline = 0;
  private int pixel = 0;
  private byte buffer = 0;
  private boolean nmi = false;

  private byte control = 0; // $2000
  private byte mask = 0; // $2001
  private byte status = 0; // $2002
  private byte oamAddress = 0; // $2003
  private short scroll = 0; // $2005
  private boolean scrollHi = true;
  private short vramAddress = 0; // $2006
  private boolean vramAddressHi = true;

  public NesPpu(byte[] chrRom, DrawFrame drawFrame) {
    this.chrRom = chrRom;
    this.ram = new byte[0x4000];
    this.oam = new byte[0x0100];
    this.palette = new byte[0x0100];
    this.drawFrame = drawFrame;
  }

  @Override
  public byte fetch(short address) {
    return switch (address) {
      case 0x00 -> readControl();
      case 0x01 -> readMask();
      case 0x02 -> readStatus();
      case 0x03 -> readOamAddress();
      case 0x04 -> readOamData();
      case 0x05 -> readScroll();
      case 0x06 -> readVramAddress();
      case 0x07 -> readVramData();
      default -> throw new IllegalArgumentException(String.format("read $20%02X", address));
    };
  }

  @Override
  public void store(short address, byte data) {
    switch (address) {
      case 0x00 -> writeControl(data);
      case 0x01 -> writeMask(data);
      case 0x03 -> writeOamAddress(data);
      case 0x04 -> writeOamData(data);
      case 0x05 -> writeScroll(data);
      case 0x06 -> writeVramAddress(data);
      case 0x07 -> writeVramData(data);
      default -> throw new IllegalArgumentException(String.format("write $20%02X", address));
    }
  }

  private byte readControl() {
    return control;
  }

  private void writeControl(byte data) {
    boolean prev_generate_nmi = bit(control, 7) == 1;

    control = data;

    boolean generate_nmi = bit(control, 7) == 1;
    boolean in_vblank = bit(status, 7) == 1;
    if (!prev_generate_nmi && generate_nmi && in_vblank) {
      nmi = true;
    }
  }

  private byte readMask() {
    return mask;
  }

  private void writeMask(byte data) {
    mask = data;
  }

  private byte readStatus() {
    byte result = status;
    status &= 0b0111_1111;
    scrollHi = true;
    vramAddressHi = true;
    return result;
  }

  private byte readOamAddress() {
    return oamAddress;
  }

  private void writeOamAddress(byte data) {
    oamAddress = data;
  }

  private byte readOamData() {
    return oam[Byte.toUnsignedInt(oamAddress)];
  }

  private void writeOamData(byte data) {
    oam[Byte.toUnsignedInt(oamAddress)] = data;
    oamAddress = (byte) ((oamAddress + 1) % oam.length);
  }

  private byte readScroll() {
    byte result = (byte) (scrollHi ? (scroll >>> 8) : (scroll & 0xff));
    return result;
  }

  private void writeScroll(byte data) {
    if (scrollHi) {
      scroll = (short) ((data << 8) | (scroll & 0x00ff));
    } else {
      scroll = (short) ((scroll & 0xff00) | data);
    }
    scrollHi = !scrollHi;
  }

  private byte readVramAddress() {
    byte result = (byte) (vramAddressHi ? (vramAddress >>> 8) : (vramAddress & 0xff));
    return result;
  }

  private void writeVramAddress(byte data) {
    if (vramAddressHi) {
      vramAddress = (short) ((data << 8) | (vramAddress & 0x00ff));
    } else {
      vramAddress = (short) ((vramAddress & 0xff00) | Byte.toUnsignedInt(data));
    }
    vramAddressHi = !vramAddressHi;
  }

  private byte readVramData() {
    short address = (short) (vramAddress & 0b0011_1111_1111_1111);
    byte result;
    if (0 <= address && address <= 0x1fff) {
      result = buffer;
      buffer = chrRom[Short.toUnsignedInt(address)];
    } else if (0x2000 <= address && address <= 0x2eff) {
      result = buffer;
      short mirror = getNametableMirrorAddress(address);
      buffer = ram[Short.toUnsignedInt(mirror)];
    } else if (0x3f00 <= address && address <= 0x3fff) {
      buffer = ram[Short.toUnsignedInt(address)];
      result = palette[address - 0x3f00];
    } else {
      throw new IllegalArgumentException("cannot read from PPU address " + address);
    }

    incrementVramAddress();
    return result;
  }

  private void writeVramData(byte data) {
    short address = (short) (vramAddress & 0b0011_1111_1111_1111);
    if (0 <= address && address <= 0x1fff) {
      chrRom[Short.toUnsignedInt(address)] = data;
    } else if (0x2000 <= address && address <= 0x2eff) {
      short mirror = getNametableMirrorAddress(address);
      ram[Short.toUnsignedInt(mirror)] = data;
    } else if (0x3f00 <= address && address <= 0x3fff) {
      palette[address - 0x3f00] = data;
    } else {
      throw new IllegalArgumentException("cannot write to PPU address " + address);
    }

    incrementVramAddress();
  }

  public void writeOamDma(byte[] buffer) {
    for (int i = 0; i < buffer.length; i++) {
      oam[Byte.toUnsignedInt(oamAddress)] = buffer[i];
      oamAddress = (byte) ((oamAddress + 1) % oam.length);
    }
  }

  private short getNametableMirrorAddress(short address) {
    boolean vertical = true;
    // Indexes:
    //   [ 0 ] [ 1 ]
    //   [ 2 ] [ 3 ]
    //
    // Horizontal mirroring:
    //   [ A ] [ a ]
    //   [ B ] [ b ]
    //
    // Vertical mirroring:
    //   [ A ] [ B ]
    //   [ a ] [ b ]
    //
    int nametable = (address - 0x2000) / 0x0400;
    return switch (nametable) {
      case 0 -> address;
      case 1 -> (short) (vertical ? address : (address - 0x0400));
      case 2 -> (short) (vertical ? (address - 0x0800) : address);
      case 3 -> (short) (vertical ? (address - 0x0800) : (address - 0x0400));
      default -> throw new IllegalArgumentException("bad nametable: " + nametable);
    };
  }

  private void incrementVramAddress() {
    vramAddress += (short) (bit(control, 2) == 1 ? 0x20 : 0x01);
  }

  public void tick() {
    pixel++;
    if (pixel == 341) {
      pixel = 0;
      scanline++;
    }
    if (scanline == 241 && pixel == 1) {
      render();
      status = (byte) (status | 0b1000_0000); // vblank
      status = (byte) (status | 0b0100_0000); // sprite 0 hit
      nmi = bit(control, 7) == 1;
    }
    if (scanline == 262) {
      scanline = 0;
      status = (byte) (status & ~0b1000_0000); // vblank
      status = (byte) (status & ~0b0100_0000); // sprite 0 hit
      nmi = false;
    }
  }

  private void render() {
    int nametable = 0x2000 + (Byte.toUnsignedInt(control) & 0b0000_0011) * 0x0400;

    // Each frame is 32x30 tiles, each tile is 8x8 pixels.
    // 32x30 tiles, 8x8 pixels
    byte[] frame = new byte[256 * 240 * 3];
    renderBackground(nametable, frame);
    renderSprites(frame);

    drawFrame.draw(frame);
  }

  private void renderBackground(int nametable, byte[] frame) {
    int bank = bit(control, 4) == 0 ? 0x000 : 0x1000;
    for (int i = 0; i < 0x03c0; i++) {
      short tileI = (short) Byte.toUnsignedInt(ram[nametable + i]);
      int tileColumn = i % 32;
      int tileRow = i / 32;

      byte[] bgPalette = getBackgroundPalette(nametable, tileColumn, tileRow);

      byte[] tile =
          Arrays.copyOfRange(chrRom, (short) (bank + tileI * 16), (short) (bank + tileI * 16 + 16));
      for (int y = 0; y < 8; y++) {
        byte upper = tile[y];
        byte lower = tile[y + 8];
        for (int x = 7; x >= 0; x--) {
          int value = ((1 & lower) << 1) | (1 & upper);
          upper >>= 1;
          lower >>= 1;
          byte[] color =
              switch (value) {
                case 0 -> SYSTEM_PALETTE[palette[0]];
                case 1 -> SYSTEM_PALETTE[bgPalette[1]];
                case 2 -> SYSTEM_PALETTE[bgPalette[2]];
                case 3 -> SYSTEM_PALETTE[bgPalette[3]];
                default -> throw new IllegalStateException();
              };
          setPixel(frame, tileColumn * 8 + x, tileRow * 8 + y, color);
        }
      }
    }
  }

  private void renderSprites(byte[] frame) {
    short bank = (short) (bit(control, 3) == 0 ? 0x0000 : 0x1000);
    for (int i = oam.length - 4; i >= 0; i -= 4) {
      short tileI = (short) Byte.toUnsignedInt(oam[i + 1]);
      int tileX = Byte.toUnsignedInt(oam[i + 3]);
      int tileY = Byte.toUnsignedInt(oam[i]);

      boolean flipVertical = bit(oam[i + 2], 7) == 1;
      boolean flipHorizontal = bit(oam[i + 2], 6) == 1;

      byte paletteI = (byte) (oam[i + 2] & 0b11);

      byte[] spritePalette = getSpritePalette(paletteI);

      byte[] tile =
          Arrays.copyOfRange(chrRom, (short) (bank + tileI * 16), (short) (bank + tileI * 16 + 16));
      for (int y = 0; y < 8; y++) {
        byte upper = tile[y];
        byte lower = tile[y + 8];
        for (int x = 7; x >= 0; x--) {
          int value = (1 & lower) << 1 | (1 & upper);
          upper >>= 1;
          lower >>= 1;
          if (value == 0) {
            continue;
          }
          byte[] color =
              switch (value) {
                case 1 -> SYSTEM_PALETTE[spritePalette[1]];
                case 2 -> SYSTEM_PALETTE[spritePalette[2]];
                case 3 -> SYSTEM_PALETTE[spritePalette[3]];
                default -> throw new IllegalStateException();
              };
          if (!flipHorizontal && !flipVertical) {
            setPixel(frame, tileX + x, tileY + y, color);
          }
          if (flipHorizontal && !flipVertical) {
            setPixel(frame, tileX + 7 - x, tileY + y, color);
          }
          if (!flipHorizontal && flipVertical) {
            setPixel(frame, tileX + x, tileY + 7 - y, color);
          }
          if (flipHorizontal && flipVertical) {
            setPixel(frame, tileX + 7 - x, tileY + 7 - y, color);
          }
        }
      }
    }
  }

  private byte[] getBackgroundPalette(int nametable, int tileX, int tileY) {
    int attrTableI = tileY / 4 * 8 + tileX / 4;
    byte attrByte = ram[nametable + 0x03c0 + attrTableI];
    int paletteI;
    int a = tileX % 4 / 2;
    int b = tileY % 4 / 2;
    if (a == 0 && b == 0) {
      paletteI = (attrByte >> 0) & 0b11;
    } else if (a == 1 && b == 0) {
      paletteI = (attrByte >> 2) & 0b11;
    } else if (a == 0 && b == 1) {
      paletteI = (attrByte >> 4) & 0b11;
    } else if (a == 1 && b == 1) {
      paletteI = (attrByte >> 6) & 0b11;
    } else {
      throw new IllegalStateException();
    }
    int paletteStart = 1 + paletteI * 4;
    return new byte[] {
      palette[0], palette[paletteStart], palette[paletteStart + 1], palette[paletteStart + 2]
    };
  }

  private byte[] getSpritePalette(byte paletteI) {
    int paletteStart = 0x11 + Byte.toUnsignedInt((byte) (paletteI * 4));
    return new byte[] {
      0, palette[paletteStart], palette[paletteStart + 1], palette[paletteStart + 2]
    };
  }

  private void setPixel(byte[] frame, int x, int y, byte[] color) {
    int base = y * 3 * 256 + x * 3;
    if (base + 2 < frame.length) {
      frame[base + 0] = color[0];
      frame[base + 1] = color[1];
      frame[base + 2] = color[2];
    }
  }

  public void reset() {
    scanline = 0;
    pixel = 0;
    nmi = false;
  }

  public int scanline() {
    return scanline;
  }

  public int column() {
    return pixel;
  }

  public boolean nmi() {
    boolean result = nmi;
    nmi = false;
    return result;
  }

  private static int bit(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }
}
