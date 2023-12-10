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
    this.ram = new byte[0x0800];
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

    t = (short) ((t & 0b0111_0011_1111_1111) | ((data & 0b0000_0011) << 10));
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
      t =
          (short)
              ((t & 0b0111_1111_1110_0000)
                  | (Byte.toUnsignedInt((byte) (data & 0b1111_1000)) >>> 3));
      x = (byte) (data & 0b0000_0111);
      w = 1;
    } else {
      t =
          (short)
              ((t & 0b0111_1100_0001_1111)
                  | (Byte.toUnsignedInt((byte) (data & 0b1111_1000)) << 2));
      t =
          (short)
              ((t & 0b0000_1111_1111_1111)
                  | (Byte.toUnsignedInt((byte) (data & 0b0000_0111)) << 12));
      w = 0;
    }
  }

  public void writeAddress(byte data) {
    if (w == 0) {
      t =
          (short)
              ((t & 0b0000_0000_1111_1111)
                  | (Byte.toUnsignedInt((byte) (data & 0b0011_1111)) << 8));
      w = 1;
    } else {
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
    if (0x2000 <= v && v < 0x2f00) {
      result = buffer;
      buffer = ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(v))];
      incrementAddress();
      return result;
    }
    if (0x3000 <= v && v < 0x3f00) {
      result = buffer;
      buffer = ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(v))];
      incrementAddress();
      return result;
    }
    if (0x3f00 <= v && v < 0x4000) {
      result = palette[getPaletteAddress(v)];
      buffer = ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(v))];
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
    if (0x2000 <= v && v < 0x2f00) {
      ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(v))] = data;
      incrementAddress();
      return;
    }
    if (0x3000 <= v && v < 0x3f00) {
      ram[Short.toUnsignedInt(mapper.getNametableMirrorAddress(v))] = data;
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
    // Coarse X increment
    if ((v & 0x001f) == 31) {
      // Coarse X wraps around.  Set it to 0 and switch horizontal nametable.
      v = (short) (v & 0b0111_1111_1110_0000);
      v = (short) (v ^ 0b0000_0100_0000_0000);
    } else {
      v += 1;
    }
  }

  private void incrementVertical() {
    // Y increment
    if ((v & 0x7000) != 0x7000) {
      v += 0x1000; // increment fine Y until it's equal to 7
    } else {
      v = (short) (v & 0b0000_1111_1111_1111); // set fine Y to 0
      int coarseY = (v & 0b0000_0011_1110_0000) >> 5;
      if (coarseY == 29) {
        // Coarse Y wraps around.  Set it to 0 and switch vertical nametable.
        coarseY = 0;
        v = (short) (v ^ 0b0000_1000_0000_0000);
      } else if (coarseY == 31) {
        // Coarse Y wraps around, BUT don't switch the vertical nametable.
        coarseY = 0;
      } else {
        coarseY += 1;
      }
      v = (short) ((v & 0b0111_1100_0001_1111) | (coarseY << 5));
    }
  }

  private void incrementOamAddress() {
    oamAddress = (byte) ((oamAddress + 1) % oam.length);
  }

  public void tick() {
    if (isRenderingEnabled()) {
      if ((0 <= scanline && scanline <= 239) || scanline == 261) {
        if ((1 <= pixel && pixel <= 256) || (321 <= pixel && pixel <= 336)) {
          int phase = pixel % 8;
          if (phase == 1) {
            // Fetch nametable byte
            short address = (short) (0x2000 | (v & 0x0fff));
            nametableByte = ram[mapper.getNametableMirrorAddress(address)];
          }
          if (phase == 3) {
            // Fetch attribute table byte
            //            attr     nametable      coarse y / 4        coarse x / 4
            int address = 0x23c0 | (v & 0x0c00) | ((v >> 4) & 0x38) | ((v >> 2) & 0x07);
            //if (address - 0x2000 < ram.length) { // FIXME
              attributeByte = ram[(address - 0x2000) % ram.length];
            //}
          }
          if (phase == 5) {
            // Fetch pattern byte lo into latch
            int chrBank = bit8(control, 4) == 0 ? 0x0000 : 0x1000;
            int y = (v & (0b0111_0000_0000_0000)) >> 12;
            int chrIndex = chrBank + Byte.toUnsignedInt(nametableByte) * 16 + y;
            patternLoLatch = mapper.readChr((short) chrIndex);
          }
          if (phase == 7) {
            // Fetch pattern byte hi into latch
            int chrBank = bit8(control, 4) == 0 ? 0x0000 : 0x1000;
            int y = (v & (0b0111_0000_0000_0000)) >> 12;
            int chrIndex = chrBank + Byte.toUnsignedInt(nametableByte) * 16 + y;
            patternHiLatch = mapper.readChr((short) (chrIndex + 8));
          }
          if (phase == 0) {
            incrementHorizontal();
            if (pixel == 256) {
              incrementVertical();
            }
          }
          if (pixel >= 9 && phase == 1) {
            // Reload shift registers
            patternLoShift =
                (short) ((patternLoShift & 0xff00) | Byte.toUnsignedInt(patternLoLatch));
            patternHiShift =
                (short) ((patternHiShift & 0xff00) | Byte.toUnsignedInt(patternHiLatch));
            attributeLoShift =
                (short) ((attributeLoShift & 0xff00) | (bit8(attributeByte, 0) == 0 ? 0x00 : 0xff));
            attributeHiShift =
                (short) ((attributeHiShift & 0xff00) | (bit8(attributeByte, 1) == 0 ? 0x00 : 0xff));
          }
        }
        if (pixel == 257) {
          // horiz(v) = horiz(t)
          int mask = 0b0000_0100_0001_1111;
          v = (short) (((v & ~mask) | (t & mask)) & 0b0111_1111_1111_1111);
        }
        if (257 <= pixel && pixel <= 320) {
          // TODO: Fetch tile data for sprites on the next scanline
        }
        if (pixel == 337 || pixel == 339) {
          // Dead reads, might be used for timing?
          short address = (short) (0x2000 | (v & 0x0fff));
          nametableByte = ram[mapper.getNametableMirrorAddress(address)];
        }
        if (scanline == 261) {
          if (280 <= pixel && pixel <= 304) {
            // vert(v) = vert(t)
            int mask = 0b0111_1011_1110_0000;
            v = (short) (((v & ~mask) | (t & mask)) & 0b0111_1111_1111_1111);
          }
        }
        if (0 <= scanline && scanline <= 239 && 1 <= pixel && pixel <= 256) {
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
          frame[i] = color.r();
          frame[i + 1] = color.g();
          frame[i + 2] = color.b();

          patternLoShift <<= 1;
          patternHiShift <<= 1;
          attributeLoShift <<= 1;
          attributeHiShift <<= 1;
        }
      }
      if (scanline == 241 && pixel == 0) {
        drawFrame.draw(frame);
        odd = !odd;
      }
    }
    if (scanline == 241) {
      // Vblank begins.
      if (pixel == 1) {
        status = (byte) (status | 0b1000_0000); // set vblank
        nmi = bit8(control, 7) == 1;
      }
    }
    if (scanline == 261) {
      // Vblank ends.
      if (pixel == 1) {
        status = (byte) (status & 0b0011_1111); // clear vblank and sprite0 hit
        nmi = false;
      }
    }
    if (scanline == 261) {
      if ((pixel == 339 && odd) || pixel == 340) {
        scanline = 0;
        pixel = 0;
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
