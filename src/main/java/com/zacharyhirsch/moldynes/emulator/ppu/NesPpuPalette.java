package com.zacharyhirsch.moldynes.emulator.ppu;

import java.io.IOException;
import java.io.InputStream;

public final class NesPpuPalette {

  private final NesPpuColor[] colors;

  public NesPpuPalette(NesPpuColor[] colors) {
    this.colors = colors;
  }

  public static NesPpuPalette load(InputStream input) throws IOException {
    NesPpuColor[] colors = new NesPpuColor[64];
    for (int i = 0; i < 64; i++) {
      int r = readByte(input);
      int g = readByte(input);
      int b = readByte(input);
      colors[i] = new NesPpuColor((byte) r, (byte) g, (byte) b);
    }
    return new NesPpuPalette(colors);
  }

  private static int readByte(InputStream input) throws IOException {
    int value = input.read();
    if (value == -1) {
      throw new IOException("unexpected end of palette");
    }
    return value;
  }

  public NesPpuColor get(int paletteIndex) {
    return colors[paletteIndex];
  }
}
