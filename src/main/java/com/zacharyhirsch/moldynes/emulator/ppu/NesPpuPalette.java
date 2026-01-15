package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.io.Color;
import java.io.IOException;
import java.io.InputStream;

public final class NesPpuPalette {

  private final Color[] colors;

  public NesPpuPalette(Color[] colors) {
    this.colors = colors;
  }

  public static NesPpuPalette load(InputStream input) throws IOException {
    Color[] colors = new Color[64];
    for (int i = 0; i < 64; i++) {
      int r = readByte(input);
      int g = readByte(input);
      int b = readByte(input);
      colors[i] = new Color((byte) r, (byte) g, (byte) b);
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

  public Color get(int paletteIndex) {
    return colors[paletteIndex];
  }
}
