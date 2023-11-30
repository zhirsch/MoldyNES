package com.zacharyhirsch.moldynes.emulator.ppu;


public final class NesPpuFrame {

  public record NesPpuPixel(NesPpuColor color, boolean backgroundPriority) {}

  private final NesPpuPixel[] frame;

  public NesPpuFrame() {
    this.frame = new NesPpuPixel[256 * 240];
  }

  public NesPpuPixel[] getPixels() {
    return frame;
  }

  public void set(int pixelI, NesPpuPixel pixel) {
    if (pixelI < frame.length && frame[pixelI] == null) {
      frame[pixelI] = pixel;
    }
  }
}
