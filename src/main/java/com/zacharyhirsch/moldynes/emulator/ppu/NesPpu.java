package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;

public final class NesPpu {

  public interface DrawFrame {

    void draw(byte[] frame);
  }

  private final NesMapper mapper;
  private final DrawFrame drawFrame;
  private final byte[] ram;
  private final byte[] oam;
  private final byte[] palette;
  private final byte[] frame = new byte[256 * 240 * 3];

  private int scanline = 0;
  private int pixel = 0;
  private byte buffer = 0;
  private boolean nmi = false;

  private byte control = 0;
  private byte mask = 0;
  private byte status = 0;
  private byte oamAddress = 0;

  private boolean odd = false;
  private short v = 0;
  private short t = 0;
  private byte x = 0;
  private byte w = 0;

  private byte nametableByte;
  private byte attributeByte;
  private byte patternLoLatch;
  private byte patternHiLatch;
  private short patternLoShift;
  private short patternHiShift;
  private short attributeLoShift;
  private short attributeHiShift;

  public NesPpu(NesMapper mapper, DrawFrame drawFrame) {
    this.mapper = mapper;
    this.drawFrame = drawFrame;
    this.ram = new byte[0x2000];
    this.oam = new byte[0x0100];
    this.palette = new byte[0x0100];
  }

  public void writeControl(byte data) {
    boolean prev_generate_nmi = bit8(control, 7) == 1;

    control = data;

    boolean generate_nmi = bit8(control, 7) == 1;
    boolean in_vblank = bit8(status, 7) == 1;
    if (!prev_generate_nmi && generate_nmi && in_vblank) {
      nmi = true;
    }

    /*
    t: ...GH.. ........ <- d: ......GH
       <used elsewhere> <- d: ABCDEF..
    */
    t = (short) ((t & 0b0111_0011_1111_1111) | ((Byte.toUnsignedInt(data) & 0b0000_0011) << 10));
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
    w = 0;
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
    if (w == 0) {
      /*
      t: ....... ...ABCDE <- d: ABCDE...
      x:              FGH <- d: .....FGH
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0111_1111_1110_0000)
                  | (byte) ((Byte.toUnsignedInt(data) & 0b0001_1111) >>> 3));
      x = (byte) (Byte.toUnsignedInt(data) & 0b0000_0111);
      w = 1;
    } else {
      /*
      t: FGH..AB CDE..... <- d: ABCDEFGH
      w:                  <- 0
      */
      t =
          (short)
              ((t & 0b0000_1100_0001_1111)
                  | ((Byte.toUnsignedInt(data) & 0b1111_1000) << 2)
                  | ((Byte.toUnsignedInt(data) & 0b0000_0111) << 12));
      w = 0;
    }
  }

  public void writeAddress(byte data) {
    if (w == 0) {
      /*
      t: .CDEFGH ........ <- d: ..CDEFGH
             <unused>     <- d: AB......
      t: Z...... ........ <- 0 (bit Z is cleared)
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0000_0000_1111_1111)
                  | (short) ((Byte.toUnsignedInt(data) << 8) & 0b0011_1111_0000_0000));
      w = 1;
    } else {
      /*
      t: ....... ABCDEFGH <- d: ABCDEFGH
      v: <...all bits...> <- t: <...all bits...>
      w:                  <- 0
      */
      t = (short) ((t & 0b0111_1111_0000_0000) | Byte.toUnsignedInt(data));
      v = t;
      w = 0;
    }
  }

  public byte readData() {
    byte result;
    if (0 <= v && v < 0x2000) {
      result = buffer;
      buffer = mapper.readChr(v);
      incrementAddress();
      return result;
    }
    if (0x2000 <= v && v < 0x3000) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3000 <= v && v < 0x3f00) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3f00 <= v && v < 0x4000) {
      result = palette[getPaletteAddress(v)];
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    throw new IllegalArgumentException("cannot read from PPU address " + v);
  }

  public void writeData(byte data) {
    if (0 <= v && v < 0x2000) {
      mapper.writeChr(v, data);
      incrementAddress();
      return;
    }
    if (0x2000 <= v && v < 0x3000) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3000 <= v && v < 0x3f00) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3f00 <= v && v < 0x4000) {
      palette[getPaletteAddress(v)] = data;
      incrementAddress();
      return;
    }
    throw new IllegalArgumentException("cannot write to PPU address " + v);
  }

  private int getPaletteAddress(int address) {
    if (address == 0x3f10 || address == 0x3f14 || address == 0x3f18 || address == 0x3f1c) {
      return address - 0x0010 - 0x3f00;
    }
    return address - 0x3f00;
  }

  private void incrementAddress() {
    if (bit8(control, 2) == 1) {
      v += 32;
    } else {
      v += 1;
    }
  }

  // https://www.nesdev.org/wiki/PPU_scrolling#Wrapping_around
  private void incrementHorizontal() {
    int coarseX = (v & 0b0000_0000_0001_1111) >>> 0;

    coarseX = (coarseX + 1) % 32;

    if (coarseX == 0) {
      // Switch horizontal nametable
      v ^= 0b0000_0100_0000_0000;
    }

    v &= 0b0111_1111_1110_0000;
    v |= (short) (coarseX << 0);
  }

  private void incrementVertical() {
    int fineY = (v & 0b0111_0000_0000_0000) >>> 12;
    int coarseY = (v & 0b0000_0011_1110_0000) >>> 5;

    fineY = (fineY + 1) % 8;

    if (fineY == 0) {
      coarseY++;
      if (coarseY == 30) {
        // Coarse Y wraps around.  Set it to 0 and switch vertical nametable.
        coarseY = 0;
        v ^= 0b0000_1000_0000_0000;
      } else if (coarseY == 32) {
        // Coarse Y wraps around, BUT don't switch the vertical nametable.
        coarseY = 0;
      }
    }

    v &= 0b0000_1100_0001_1111;
    v |= (short) (fineY << 12);
    v |= (short) (coarseY << 5);
  }

  private void incrementOamAddress() {
    oamAddress = (byte) ((oamAddress + 1) % oam.length);
  }

  public void tick() {
    if (isRenderingEnabled()) {
      if (0 <= scanline && scanline <= 239) {
        doVisibleScanline(false);
      }
      if (scanline == 240) {
        doPostRenderScanline();
        if (pixel == 0) {
          drawFrame.draw(frame);
        }
      }
    }
    if (241 <= scanline && scanline <= 260) {
      doVerticalBlankingScanline();
    }
    if (scanline == 261) {
      doVisibleScanline(true);
    }
    if (scanline == 261) {
      if ((pixel == 339 && odd) || pixel == 340) {
        scanline = 0;
        pixel = 0;
        odd = !odd;
      } else {
        pixel++;
      }
    } else {
      if (pixel == 340) {
        scanline++;
        pixel = 0;
      } else {
        pixel++;
      }
    }
  }

  private void doVisibleScanline(boolean isPreRender) {
    if (pixel == 0) {
      idle();
      return;
    }
    if (1 <= pixel && pixel <= 256) {
      if (pixel == 1 && isPreRender) {
        // clear vblank and sprite0 hit
        status = (byte) (status & 0b0011_1111);
        nmi = false;
      }
      if (!isPreRender) {
        renderPixel();
      }
      shiftRegisters();
      // Fetch tile data for this scanline
      fetchTileData((pixel - 1) % 8);
      if (pixel % 8 == 0) {
        incrementHorizontal();
        reloadShiftRegisters();
      }
      if (pixel == 256) {
        incrementVertical();
      }
      return;
    }
    if (257 <= pixel && pixel <= 320) {
      if (pixel == 257) {
        // horiz(v) = horiz(t)
        // v: ....A.. ...BCDEF <- t: ....A.. ...BCDEF
        v &= 0b0111_1011_1110_0000;
        v |= (short) (t & 0b0000_0100_0001_1111);
      }
      if (isPreRender && 280 <= pixel && pixel <= 304) {
        // vert(v) = vert(t)
        // v: GHIA.BC DEF..... <- t: GHIA.BC DEF.....
        v &= 0b0000_0100_0001_1111;
        v |= (short) (t & 0b0111_1011_1110_0000);
      }
      // Fetch sprite data for the *next* scanline
      fetchSpriteData((pixel - 1) % 8);
      return;
    }
    if (321 <= pixel && pixel <= 336) {
      shiftRegisters();
      // Fetch the first two tiles for the *next* scanline
      fetchTileData((pixel - 1) % 8);
      if (pixel % 8 == 0) {
        incrementHorizontal();
        reloadShiftRegisters();
      }
      return;
    }
    if (337 <= pixel && pixel <= 340) {
      // Fetch unused bytes
      fetchUnusedBytes((pixel - 1) % 4);
      return;
    }
    // TODO: sprite evaluation for the next scanline
    throw new IllegalStateException();
  }

  private void renderPixel() {
    int patternHi = bit16(patternHiShift, 15 - x);
    int patternLo = bit16(patternLoShift, 15 - x);
    int attributeHi = bit16(attributeHiShift, 15 - x);
    int attributeLo = bit16(attributeLoShift, 15 - x);
    NesPpuColor[] colors = getBackgroundPalette((attributeHi << 1) | attributeLo);
    NesPpuColor color =
        switch ((patternHi << 1) | patternLo) {
          case 0 -> colors[0];
          case 1 -> colors[1];
          case 2 -> colors[2];
          case 3 -> colors[3];
          default -> throw new IllegalStateException();
        };
    int i = 3 * (scanline * 256 + pixel - 1);
    frame[i + 0] = color.r();
    frame[i + 1] = color.g();
    frame[i + 2] = color.b();
  }

  private void shiftRegisters() {
    patternLoShift <<= 1;
    patternHiShift <<= 1;
    attributeLoShift <<= 1;
    attributeHiShift <<= 1;
  }

  private void idle() {}

  private void fetchTileData(int phase) {
    if (phase == 0) {
      // start fetch of nametable byte
      return;
    }
    if (phase == 1) {
      // finish fetch of nametable byte
      nametableByte = fetchNametableByte();
      return;
    }
    if (phase == 2) {
      // start fetch of attribute table byte
      return;
    }
    if (phase == 3) {
      // finish fetch of attribute table byte
      attributeByte = fetchAttributeByte();
      return;
    }
    if (phase == 4) {
      // start fetch of pattern table tile low byte into latch
      return;
    }
    if (phase == 5) {
      // finish fetch of pattern table tile low byte into latch
      patternLoLatch = fetchPatternTableByte(0);
      return;
    }
    if (phase == 6) {
      // start fetch of pattern table tile high byte into latch
      return;
    }
    if (phase == 7) {
      // finish fetch of pattern table tile high byte into latch
      patternHiLatch = fetchPatternTableByte(8);
      return;
    }
    throw new IllegalStateException();
  }

  private void fetchSpriteData(int phase) {
    // TODO: fetch sprite data
  }

  private void fetchUnusedBytes(int phase) {
    if (phase == 0) {
      // start fetch of first unused byte
      return;
    }
    if (phase == 1) {
      // finish fetch of first unused byte
      fetchNametableByte();
      return;
    }
    if (phase == 2) {
      // start fetch of second unused byte
      return;
    }
    if (phase == 3) {
      // finish fetch of second unused byte
      fetchNametableByte();
      return;
    }
    throw new IllegalStateException();
  }

  private byte fetchNametableByte() {
    short address = (short) (0x2000 | (v & 0x0fff));
    //    return ram[mapper.getNametableMirrorAddress(address)];
    return ram[v & 0x0fff];
  }

  private byte fetchAttributeByte() {
    //    short mainNametable =
    //        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
    //          case 0 -> 0x0000;
    //          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
    //          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
    //          case 3 -> 0x0400;
    //          default -> throw new IllegalStateException();
    //        };
    //
    //    int tileRow = scanline / 8;
    //    int tileColumn = pixel / 8;
    //    byte attrByte = ram[nametable + 0x03c0 + tileRow / 4 * 8 + tileColumn / 4];

    int nametab = (v >> 0) & 0b0000_1100_0000_0000;
    int coarseY = (v >> 4) & 0b0000_0000_0011_1000;
    int coarseX = (v >> 2) & 0b0000_0000_0000_0111;
    int address = 0b0000_0011_1100_0000 | nametab | coarseY | coarseX;
    //    return ram[address % ram.length];
    return ram[address];
  }

  private byte fetchPatternTableByte(int offset) {
    int chrBank = bit8(control, 4) == 0 ? 0b0000_0000_0000_0000 : 0b0001_0000_0000_0000;
    int fineY = (v >> 12) & 0b0111;
    int chrIndex = chrBank | (Byte.toUnsignedInt(nametableByte) << 4) | offset | fineY;
    return mapper.readChr((short) chrIndex);
  }

  private void doPostRenderScanline() {
    idle();
  }

  private void doVerticalBlankingScanline() {
    if (pixel == 1 && scanline == 241) {
      // set vblank
      status = (byte) (status | 0b1000_0000);
      nmi = bit8(control, 7) == 1;
      return;
    }
    idle();
  }

  private void reloadShiftRegisters() {
    attributeLoShift &= (short) 0xff00;
    attributeLoShift |= (short) (bit8(attributeByte, 0) == 0 ? 0x0000 : 0x00ff);

    attributeHiShift &= (short) 0xff00;
    attributeHiShift |= (short) (bit8(attributeByte, 1) == 0 ? 0x0000 : 0x00ff);

    patternLoShift &= (short) 0xff00;
    patternLoShift |= (short) Byte.toUnsignedInt(patternLoLatch);

    patternHiShift &= (short) 0xff00;
    patternHiShift |= (short) Byte.toUnsignedInt(patternHiLatch);
  }

  private NesPpuColor[] getBackgroundPalette(int attribute) {
    return new NesPpuColor[] {
      NesPpuPalette.SYSTEM_PALETTE[palette[0]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 1]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 2]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 3]],
    };
  }

  private boolean isRenderingEnabled() {
    return bit8(mask, 3) == 1 || bit8(mask, 4) == 1;
  }

  public boolean nmi() {
    boolean result = nmi;
    nmi = false;
    return result;
  }

  private static int bit8(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }

  private int bit16(short value, int i) {
    return (Short.toUnsignedInt(value) >>> i) & 1;
  }
}
