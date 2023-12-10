package com.zacharyhirsch.moldynes.emulator.ppu;

public final class NesPpuPalette {

  public static final NesPpuColor[] SYSTEM_PALETTE =
      new NesPpuColor[] {
        new NesPpuColor((byte) 0x65, (byte) 0x65, (byte) 0x65),
        new NesPpuColor((byte) 0x00, (byte) 0x2b, (byte) 0x9b),
        new NesPpuColor((byte) 0x11, (byte) 0x0e, (byte) 0xc0),
        new NesPpuColor((byte) 0x3f, (byte) 0x00, (byte) 0xbc),
        new NesPpuColor((byte) 0x66, (byte) 0x00, (byte) 0x8f),
        new NesPpuColor((byte) 0x7b, (byte) 0x00, (byte) 0x45),
        new NesPpuColor((byte) 0x79, (byte) 0x01, (byte) 0x00),
        new NesPpuColor((byte) 0x60, (byte) 0x1c, (byte) 0x00),
        new NesPpuColor((byte) 0x36, (byte) 0x38, (byte) 0x00),
        new NesPpuColor((byte) 0x08, (byte) 0x4f, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x5a, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x57, (byte) 0x02),
        new NesPpuColor((byte) 0x00, (byte) 0x45, (byte) 0x55),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0xae, (byte) 0xae, (byte) 0xae),
        new NesPpuColor((byte) 0x07, (byte) 0x61, (byte) 0xf5),
        new NesPpuColor((byte) 0x3e, (byte) 0x3b, (byte) 0xff),
        new NesPpuColor((byte) 0x7c, (byte) 0x1d, (byte) 0xff),
        new NesPpuColor((byte) 0xaf, (byte) 0x0e, (byte) 0xe5),
        new NesPpuColor((byte) 0xcb, (byte) 0x13, (byte) 0x83),
        new NesPpuColor((byte) 0xc8, (byte) 0x2a, (byte) 0x15),
        new NesPpuColor((byte) 0xa7, (byte) 0x4d, (byte) 0x00),
        new NesPpuColor((byte) 0x6f, (byte) 0x72, (byte) 0x00),
        new NesPpuColor((byte) 0x37, (byte) 0x91, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x9f, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x9b, (byte) 0x2a),
        new NesPpuColor((byte) 0x00, (byte) 0x84, (byte) 0x98),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0xff, (byte) 0xff, (byte) 0xff),
        new NesPpuColor((byte) 0x56, (byte) 0xb1, (byte) 0xff),
        new NesPpuColor((byte) 0x8e, (byte) 0x8b, (byte) 0xff),
        new NesPpuColor((byte) 0xcc, (byte) 0x6c, (byte) 0xff),
        new NesPpuColor((byte) 0xff, (byte) 0x5d, (byte) 0xff),
        new NesPpuColor((byte) 0xff, (byte) 0x62, (byte) 0xd4),
        new NesPpuColor((byte) 0xff, (byte) 0x79, (byte) 0x64),
        new NesPpuColor((byte) 0xf8, (byte) 0x9d, (byte) 0x06),
        new NesPpuColor((byte) 0xc0, (byte) 0xc3, (byte) 0x00),
        new NesPpuColor((byte) 0x81, (byte) 0xe2, (byte) 0x00),
        new NesPpuColor((byte) 0x4d, (byte) 0xf1, (byte) 0x16),
        new NesPpuColor((byte) 0x30, (byte) 0xec, (byte) 0x7a),
        new NesPpuColor((byte) 0x34, (byte) 0xd5, (byte) 0xea),
        new NesPpuColor((byte) 0x4e, (byte) 0x4e, (byte) 0x4e),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0xff, (byte) 0xff, (byte) 0xff),
        new NesPpuColor((byte) 0xba, (byte) 0xdf, (byte) 0xff),
        new NesPpuColor((byte) 0xd1, (byte) 0xd0, (byte) 0xff),
        new NesPpuColor((byte) 0xeb, (byte) 0xc3, (byte) 0xff),
        new NesPpuColor((byte) 0xff, (byte) 0xbd, (byte) 0xff),
        new NesPpuColor((byte) 0xff, (byte) 0xbf, (byte) 0xee),
        new NesPpuColor((byte) 0xff, (byte) 0xc8, (byte) 0xc0),
        new NesPpuColor((byte) 0xfc, (byte) 0xd7, (byte) 0x99),
        new NesPpuColor((byte) 0xef, (byte) 0xe7, (byte) 0x84),
        new NesPpuColor((byte) 0xcc, (byte) 0xf3, (byte) 0x87),
        new NesPpuColor((byte) 0xb6, (byte) 0xf9, (byte) 0xa0),
        new NesPpuColor((byte) 0xaa, (byte) 0xf8, (byte) 0xc9),
        new NesPpuColor((byte) 0xac, (byte) 0xee, (byte) 0xf7),
        new NesPpuColor((byte) 0xb7, (byte) 0xb7, (byte) 0xb7),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
        new NesPpuColor((byte) 0x00, (byte) 0x00, (byte) 0x00),
      };

  private final byte[] palette;

  public NesPpuPalette() {
    this.palette = new byte[0x0100];
  }

  public NesPpuColor[] getSpritePalette(byte attrByte) {
    int paletteIndex = getSpritePaletteIndex(attrByte);
    return new NesPpuColor[] {
      null,
      SYSTEM_PALETTE[palette[16 + paletteIndex * 4 + 1]],
      SYSTEM_PALETTE[palette[16 + paletteIndex * 4 + 2]],
      SYSTEM_PALETTE[palette[16 + paletteIndex * 4 + 3]],
    };
  }

  private int getSpritePaletteIndex(byte attrByte) {
    return attrByte & 0b0000_0011;
  }
}
