package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import java.awt.Rectangle;
import java.util.Arrays;

final class NesPpuRenderer {

  private record NesPpuPixel(NesPpuColor color, boolean backgroundPriority) {}

  private final NesMapper mapper;
  private final NesPpuPalette palette;
  private final byte[] ram;
  private final byte[] oam;
  private final byte[] oam2;

  private NesPpuPixel[] backgroundPixels = new NesPpuPixel[256 * 240];
  private NesPpuPixel[] spritesPixels = new NesPpuPixel[256 * 240];

  NesPpuRenderer(NesMapper mapper, NesPpuPalette palette, byte[] ram, byte[] oam) {
    this.mapper = mapper;
    this.palette = palette;
    this.ram = ram;
    this.oam = oam;
    this.oam2 = new byte[32];
    Arrays.fill(oam2, (byte) 0xff);
  }

//  public boolean render(
//      byte control,
//      byte mask,
//      short v,
//      short x,
//      int scanline,
//      int pixel) {
//    if (bit(mask, 3) == 1) {
//      renderBackground(control, v, x, scanline, pixel);
//    }
//    boolean sprite0hit = false;
//    if (bit(mask, 4) == 1) {
//      sprite0hit = renderSprites(control, scanline, pixel);
//    }
//    return sprite0hit;
//  }
//
//  public byte[] finish() {
//    byte[] frame = new byte[256 * 240 * 3];
//    for (int i = 0; i < backgroundPixels.length; i++) {
//      NesPpuColor color = getPixelColor(backgroundPixels[i], spritesPixels[i]);
//      frame[(i * 3)] = color.r();
//      frame[(i * 3) + 1] = color.g();
//      frame[(i * 3) + 2] = color.b();
//    }
//    this.backgroundPixels = new NesPpuPixel[256 * 240];
//    this.spritesPixels = new NesPpuPixel[256 * 240];
//    return frame;
//  }
//
//  private NesPpuColor getPixelColor(NesPpuPixel backgroundPixel, NesPpuPixel spritePixel) {
//    if (backgroundPixel == null && spritePixel == null) {
//      return palette.getColor(0);
//    }
//    if (backgroundPixel != null && (spritePixel == null || spritePixel.backgroundPriority())) {
//      return backgroundPixel.color();
//    }
//    return spritePixel.color();
//  }
//
//  private void renderBackground(
//      byte control, short v, short x, int scanline, int pixel) {
//    short mainNametable =
//        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
//          case 0 -> 0x0000;
//          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
//          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
//          case 3 -> 0x0400;
//          default -> throw new IllegalStateException();
//        };
//    short secondNametable =
//        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
//          case 0 -> 0x0400;
//          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
//          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
//          case 3 -> 0x0000;
//          default -> throw new IllegalStateException();
//        };
//
//    int scrollX = Byte.toUnsignedInt(scroll.getX());
//    int scrollY = Byte.toUnsignedInt(scroll.getY());
//
//    {
//      Rectangle viewport = new Rectangle(scrollX, scrollY, 256, 240);
//      renderBackgroundNametable(mainNametable, viewport, control, scanline, pixel);
//    }
//
//    if (scrollX > 0) {
//      Rectangle viewport = new Rectangle(0, 0, scrollX, 240);
//      renderBackgroundNametable(secondNametable, viewport, control, scanline, pixel);
//    } else if (scrollY > 0) {
//      Rectangle viewport = new Rectangle(0, 0, 256, scrollY);
//      renderBackgroundNametable(secondNametable, viewport, control, scanline, pixel);
//    }
//  }
//
//  private void renderBackgroundNametable(
//      short nametable,
//      Rectangle viewport,
//      byte control,
//      int scanline,
//      int pixel) {
//    int tileRow = scanline / 8;
//    int tileColumn = pixel / 8;
//    short chrBank = (short) (bit(control, 4) == 0 ? 0x000 : 0x1000);
//    byte attrByte = ram[nametable + 0x03c0 + tileRow / 4 * 8 + tileColumn / 4];
//    int tileIdx = Byte.toUnsignedInt(ram[nametable + tileRow * 32 + tileColumn]);
//    int shiftX = 256 - viewport.width - viewport.x;
//    int shiftY = 240 - viewport.height - viewport.y;
//    int y = scanline - tileRow * 8;
//    int x = pixel - tileColumn * 8;
//    int pixelIdx = (shiftY + scanline) * 256 + shiftX + pixel;
//    NesPpuColor color =
//        getTileColor(
//            palette.getBackgroundPalette(tileRow, tileColumn, attrByte),
//            chrBank + tileIdx * 16 + y,
//            x);
//    if (viewport.contains(pixel, scanline)
//        && pixelIdx < backgroundPixels.length
//        && backgroundPixels[pixelIdx] == null
//        && color != null) {
//      backgroundPixels[pixelIdx] = new NesPpuPixel(color, false);
//    }
//  }
//
//  private boolean renderSprites(byte control, int scanline, int pixel) {
//    if (pixel == 0) {
//      findSpritesOnLine(control, scanline);
//    }
//    boolean sprite0hit = false;
//    for (int spriteIndex = 0; spriteIndex < oam2.length - 3; spriteIndex += 4) {
//      boolean hit = renderSprite(control, spriteIndex, scanline, pixel);
//      if (spriteIndex == 0) {
//        sprite0hit = hit;
//      }
//    }
//    return sprite0hit;
//  }
//
//  private void findSpritesOnLine(byte control, int scanline) {
//    Arrays.fill(oam2, (byte) 0xff);
//    int i = 0;
//    for (int spriteIndex = 0; spriteIndex < oam.length - 3; spriteIndex += 4) {
//      if (isSpriteOnLine(control, scanline, spriteIndex)) {
//        System.arraycopy(oam, spriteIndex, oam2, i, 4);
//        i += 4;
//      }
//      if (i >= oam2.length) {
//        break;
//      }
//    }
//  }
//
//  private boolean isSpriteOnLine(byte control, int scanline, int spriteIndex) {
//    byte byte0 = oam[spriteIndex];
//    byte byte1 = oam[spriteIndex + 1];
//    byte byte2 = oam[spriteIndex + 2];
//    byte byte3 = oam[spriteIndex + 3];
//
//    for (int x = 0; x < 256; x++) {
//      NesPpuColor color = getSpriteTileColor(control, scanline, x, byte0, byte1, byte2, byte3);
//      if (color != null) {
//        return true;
//      }
//    }
//    return false;
//  }
//
//  private boolean renderSprite(byte control, int spriteIndex, int scanline, int pixel) {
//    byte byte0 = oam2[spriteIndex];
//    byte byte1 = oam2[spriteIndex + 1];
//    byte byte2 = oam2[spriteIndex + 2];
//    byte byte3 = oam2[spriteIndex + 3];
//
//    if (byte0 == (byte) 0xff) {
//      return false;
//    }
//
//    NesPpuColor color = getSpriteTileColor(control, scanline, pixel, byte0, byte1, byte2, byte3);
//    if (color == null) {
//      return false;
//    }
//
//    boolean backgroundPriority = bit(byte2, 5) == 1;
//    spritesPixels[scanline * 256 + pixel] = new NesPpuPixel(color, backgroundPriority);
//    return true;
//  }
//
//  private NesPpuColor getSpriteTileColor(
//      byte control, int scanline, int pixel, byte byte0, byte byte1, byte byte2, byte byte3) {
//    boolean sprite8x16 = bit(control, 5) == 1;
//    int height = sprite8x16 ? 16 : 8;
//
//    int tileY = Byte.toUnsignedInt(byte0) + 1;
//    int tileX = Byte.toUnsignedInt(byte3);
//    boolean flipH = bit(byte2, 6) == 1;
//    boolean flipV = bit(byte2, 7) == 1;
//
//    int x = flipH ? (7 + tileX) - pixel : pixel - tileX;
//    if (pixel < tileX || pixel >= tileX + 8) {
//      return null;
//    }
//
//    int y = flipV ? ((height - 1) + tileY) - scanline : scanline - tileY;
//    if (scanline < tileY || scanline >= tileY + height) {
//      return null;
//    }
//
//    int chrIndex = getSpriteChrIndex(sprite8x16, control, byte1) + 8 * (y / 8);
//    return getTileColor(palette.getSpritePalette(byte2), chrIndex + y, x);
//  }
//
//  private static int getSpriteChrIndex(boolean sprite8x16, byte control, byte byte1) {
//    int offset, chrBank;
//    if (sprite8x16) {
//      offset = Byte.toUnsignedInt((byte) (byte1 & 0b1111_1110));
//      chrBank = bit(byte1, 0) * 0x1000;
//    } else {
//      offset = Byte.toUnsignedInt(byte1);
//      chrBank = bit(control, 3) * 0x1000;
//    }
//    return chrBank + offset * 16;
//  }
//
//  private NesPpuColor getTileColor(NesPpuColor[] palette, int chrIndex, int x) {
//    byte lsb = mapper.readChr((short) chrIndex);
//    byte msb = mapper.readChr((short) (chrIndex + 8));
//    return switch (bit(msb, 7 - x) << 1 | bit(lsb, 7 - x)) {
//      case 0b00 -> null;
//      case 0b01 -> palette[1];
//      case 0b10 -> palette[2];
//      case 0b11 -> palette[3];
//      default -> throw new IllegalStateException();
//    };
//  }
//
//  private static int bit(byte value, int i) {
//    return (Byte.toUnsignedInt(value) >>> i) & 1;
//  }
}
