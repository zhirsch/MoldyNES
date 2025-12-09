package com.zacharyhirsch.moldynes.emulator.ppu;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class NesPpuPalette {

  private final NesPpuColor[] colors;

  public NesPpuPalette(NesPpuColor[] colors) {
    this.colors = colors;
  }

  public static NesPpuPalette load(String path) {
    try (InputStream input = Resources.getResource(path).openStream()) {
      return load(ByteBuffer.wrap(input.readAllBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static NesPpuPalette load(ByteBuffer buffer) {
    NesPpuColor[] colors = new NesPpuColor[64];
    for (int i = 0; i < 64; i++) {
      ByteBuffer rgb = buffer.slice(3 * i, 3);
      colors[i] = new NesPpuColor(rgb.get(0), rgb.get(1), rgb.get(2));
    }
    return new NesPpuPalette(colors);
  }

  public NesPpuColor get(int paletteIndex) {
    return colors[paletteIndex];
  }
}
