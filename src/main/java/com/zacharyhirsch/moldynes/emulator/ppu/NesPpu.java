package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuFrame.NesPpuPixel;
import com.zacharyhirsch.moldynes.emulator.ppu.registers.NesPpuAddress;
import com.zacharyhirsch.moldynes.emulator.ppu.registers.NesPpuScroll;
import java.awt.Rectangle;

public final class NesPpu {

  public interface DrawFrame {

    void draw(byte[] frame);
  }

  private final NesMapper mapper;
  private final byte[] ram;
  private final byte[] oam;
  private final NesPpuPalette palette;
  private final DrawFrame drawFrame;

  private int scanline = 0;
  private int pixel = 0;
  private byte buffer = 0;
  private boolean nmi = false;

  private byte control = 0;
  private byte mask = 0;
  private byte status = 0;
  private byte oamAddress = 0;
  private final NesPpuScroll scroll = new NesPpuScroll();
  private final NesPpuAddress address = new NesPpuAddress();

  public NesPpu(NesMapper mapper, DrawFrame drawFrame) {
    this.mapper = mapper;
    this.ram = new byte[0x0800];
    this.oam = new byte[0x0100];
    this.palette = new NesPpuPalette();
    this.drawFrame = drawFrame;
  }

  public void writeControl(byte data) {
    boolean prev_generate_nmi = bit(control, 7) == 1;

    control = data;

    boolean generate_nmi = bit(control, 7) == 1;
    boolean in_vblank = bit(status, 7) == 1;
    if (!prev_generate_nmi && generate_nmi && in_vblank) {
      nmi = true;
    }
  }

  public byte readMask() {
    return mask;
  }

  public void writeMask(byte data) {
    mask = data;
  }

  public byte readStatus() {
    byte result = status;
    status &= 0b0111_1111;
    scroll.reset();
    address.reset();
    return result;
  }

  public void writeOamAddr(byte data) {
    oamAddress = data;
  }

  public byte readOamData() {
    return oam[Byte.toUnsignedInt(oamAddress)];
  }

  public void writeOamData(byte data) {
    oam[Byte.toUnsignedInt(oamAddress)] = data;
    incrementOamAddress();
  }

  public void writeOamDma(byte[] buffer) {
    for (byte b : buffer) {
      oam[Byte.toUnsignedInt(oamAddress)] = b;
      incrementOamAddress();
    }
  }

  public void writeScroll(byte data) {
    scroll.update(data);
  }

  public void writeAddress(byte data) {
    address.update(data);
  }

  public byte readData() {
    short address = this.address.get();
    byte result;
    if (0 <= address && address < 0x2000) {
      result = buffer;
      buffer = mapper.readChr(address);
      this.address.increment(getAddressIncrement());
      return result;
    }
    if (0x2000 <= address && address < 0x2f00) {
      result = buffer;
      buffer = ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(address))];
      this.address.increment(getAddressIncrement());
      return result;
    }
    if (0x3000 <= address && address < 0x3f00) {
      result = buffer;
      buffer = ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(address))];
      this.address.increment(getAddressIncrement());
      return result;
    }
    if (0x3f00 <= address && address < 0x4000) {
      result = palette.read(address);
      buffer = ram[Short.toUnsignedInt(address)];
      this.address.increment(getAddressIncrement());
      return result;
    }
    throw new IllegalArgumentException("cannot read from PPU address " + address);
  }

  public void writeData(byte data) {
    short address = this.address.get();
    if (0 <= address && address < 0x2000) {
      mapper.writeChr(address, data);
      this.address.increment(getAddressIncrement());
      return;
    }
    if (0x2000 <= address && address < 0x2f00) {
      ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(address))] = data;
      this.address.increment(getAddressIncrement());
      return;
    }
    if (0x3000 <= address && address < 0x3f00) {
      ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(address))] = data;
      this.address.increment(getAddressIncrement());
      return;
    }
    if (0x3f00 <= address && address < 0x4000) {
      palette.write(address, data);
      this.address.increment(getAddressIncrement());
      return;
    }
    throw new IllegalArgumentException("cannot write to PPU address " + address);
  }

  private byte getAddressIncrement() {
    return (byte) (bit(control, 2) == 1 ? 0x20 : 0x01);
  }

  private void incrementOamAddress() {
    oamAddress = (byte) ((oamAddress + 1) % oam.length);
  }

  public void tick() {
    pixel++;
    if (pixel == 341) {
      if (isSprite0Hit()) {
        status = (byte) (status | 0b0100_0000); // sprite 0 hit
      }
      pixel = 0;
      scanline++;
    }
    if (scanline == 241 && pixel == 1) {
      render();
      status = (byte) (status | 0b1000_0000); // vblank
      status = (byte) (status & ~0b0100_0000); // sprite 0 hit
      nmi = bit(control, 7) == 1;
    }
    if (scanline == 262 && pixel == 1) {
      scanline = 0;
      status = (byte) (status & ~0b1000_0000); // vblank
      status = (byte) (status & ~0b0100_0000); // sprite 0 hit
      nmi = false;
    }
  }

  private boolean isSprite0Hit() {
    byte y = oam[0];
    byte x = oam[3];
    // TODO: check for transparency
    return (y == scanline) && x <= pixel && bit(mask, 4) == 1;
  }

  private void render() {
    NesPpuFrame background = new NesPpuFrame();
    NesPpuFrame sprites = new NesPpuFrame();
    if (bit(mask, 3) == 1) {
      renderBackground(background);
    }
    if (bit(mask, 4) == 1) {
      renderSprites(sprites);
    }

    byte[] frame = new byte[256 * 240 * 3];
    NesPpuPixel[] backgroundPixels = background.getPixels();
    NesPpuPixel[] spritesPixels = sprites.getPixels();
    for (int i = 0; i < backgroundPixels.length; i++) {
      NesPpuColor color = palette.getColor(0);
      NesPpuPixel backgroundPixel = backgroundPixels[i];
      if (backgroundPixel != null) {
        color = backgroundPixel.color();
      }
      NesPpuPixel spritePixel = spritesPixels[i];
      if (spritePixel != null) {
        if (!spritePixel.backgroundPriority()) {
          color = spritePixel.color();
        } else if (backgroundPixel == null) {
          color = spritePixel.color();
        }
      }
      if (color != null) {
        frame[(i * 3)] = color.r();
        frame[(i * 3) + 1] = color.g();
        frame[(i * 3) + 2] = color.b();
      }
    }

    drawFrame.draw(frame);
  }

  private void renderBackground(NesPpuFrame frame) {
    short mainNametable =
        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
          case 0 -> 0x0000;
          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
          case 3 -> 0x0400;
          default -> throw new IllegalStateException();
        };
    short secondNametable =
        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
          case 0 -> 0x0400;
          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
          case 3 -> 0x0000;
          default -> throw new IllegalStateException();
        };

    int scrollX = Byte.toUnsignedInt(scroll.getX());
    int scrollY = Byte.toUnsignedInt(scroll.getY());

    {
      Rectangle viewport = new Rectangle(scrollX, scrollY, 256, 240);
      renderBackgroundNametable(mainNametable, viewport, frame);
    }

    if (scrollX > 0) {
      Rectangle viewport = new Rectangle(0, 0, scrollX, 240);
      renderBackgroundNametable(secondNametable, viewport, frame);
    } else if (scrollY > 0) {
      Rectangle viewport = new Rectangle(0, 0, 256, scrollY);
      renderBackgroundNametable(secondNametable, viewport, frame);
    }
  }

  private void renderBackgroundNametable(short nametable, Rectangle viewport, NesPpuFrame frame) {
    short chrBank = (short) (bit(control, 4) == 0 ? 0x000 : 0x1000);
    for (int tileRow = 0; tileRow < 30; tileRow++) {
      for (int tileColumn = 0; tileColumn < 32; tileColumn++) {
        int tileIdx = Byte.toUnsignedInt(ram[nametable + tileRow * 32 + tileColumn]);

        byte attrByte = ram[nametable + 0x03c0 + tileRow / 4 * 8 + tileColumn / 4];
        NesPpuColor[] palette = this.palette.getBackgroundPalette(tileRow, tileColumn, attrByte);

        int shiftX = 256 - viewport.width - viewport.x;
        int shiftY = 240 - viewport.height - viewport.y;
        for (int y = 0; y < 8; y++) {
          byte upper = mapper.readChr((short) (chrBank + tileIdx * 16 + y));
          byte lower = mapper.readChr((short) (chrBank + tileIdx * 16 + y + 8));
          for (int x = 7; x >= 0; x--) {
            int value = ((1 & lower) << 1) | (1 & upper);
            upper >>= 1;
            lower >>= 1;
            NesPpuColor color =
                switch (value) {
                  case 0 -> null;
                  case 1 -> palette[1];
                  case 2 -> palette[2];
                  case 3 -> palette[3];
                  default -> throw new IllegalStateException();
                };
            if (color == null) {
              continue;
            }
            int pixelX = tileColumn * 8 + x;
            int pixelY = tileRow * 8 + y;
            if (viewport.contains(pixelX, pixelY)) {
              int pixelIdx = (shiftY + pixelY) * 256 + shiftX + pixelX;
              frame.set(pixelIdx, new NesPpuPixel(color, false));
            }
          }
        }
      }
    }
  }

  private void renderSprites(NesPpuFrame frame) {
    boolean sprites8x16 = bit(control, 5) == 1;
    Rectangle viewport = new Rectangle(0, 0, 256, 240);
    for (int spriteIndex = 0; spriteIndex < oam.length - 3; spriteIndex += 4) {
      byte byte0 = oam[spriteIndex];
      byte byte1 = oam[spriteIndex + 1];
      byte byte2 = oam[spriteIndex + 2];
      byte byte3 = oam[spriteIndex + 3];

      int tileX = Byte.toUnsignedInt(byte3);
      int tileY = Byte.toUnsignedInt(byte0);

      int tileIdx =
          sprites8x16
              ? Byte.toUnsignedInt((byte) (byte1 & 0b1111_1110))
              : Byte.toUnsignedInt(byte1);
      short chrBank =
          sprites8x16
              ? (short) (bit(byte1, 0) == 0 ? 0x0000 : 0x1000)
              : (short) (bit(control, 3) == 0 ? 0x0000 : 0x1000);

      NesPpuColor[] palette = this.palette.getSpritePalette(byte2);
      boolean flipH = bit(byte2, 6) == 1;
      boolean flipV = bit(byte2, 7) == 1;
      boolean backgroundPriority = bit(byte2, 5) == 1;

      renderSprite(
          frame,
          chrBank,
          tileIdx,
          tileX,
          tileY,
          0,
          sprites8x16 ? 16 : 8,
          palette,
          viewport,
          flipH,
          flipV,
          backgroundPriority);
      if (sprites8x16) {
        renderSprite(
            frame,
            chrBank,
            tileIdx,
            tileX,
            tileY,
            8,
            16,
            palette,
            viewport,
            flipH,
            flipV,
            backgroundPriority);
      }
    }
  }

  private void renderSprite(
      NesPpuFrame frame,
      short chrBank,
      int tileIdx,
      int tileX,
      int tileY,
      int startY,
      int height,
      NesPpuColor[] palette,
      Rectangle viewport,
      boolean flipH,
      boolean flipV,
      boolean backgroundPriority) {
    for (int y = startY; y < startY + 8; y++) {
      byte upper = mapper.readChr((short) (chrBank + tileIdx * 16 + startY + y));
      byte lower = mapper.readChr((short) (chrBank + tileIdx * 16 + startY + y + 8));
      for (int x = 7; x >= 0; x--) {
        int value = ((1 & lower) << 1) | (1 & upper);
        upper >>= 1;
        lower >>= 1;
        NesPpuColor color =
            switch (value) {
              case 0 -> null;
              case 1 -> palette[1];
              case 2 -> palette[2];
              case 3 -> palette[3];
              default -> throw new IllegalStateException();
            };
        if (color == null) {
          continue;
        }
        int pixelX = flipH ? (tileX + 7 - x) : (tileX + x);
        int pixelY = 1 + (flipV ? (tileY + (height - 1) - y) : (tileY + y));
        if (viewport.contains(pixelX, pixelY)) {
          frame.set(pixelY * 256 + pixelX, new NesPpuPixel(color, backgroundPriority));
        }
      }
    }
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
