package com.zacharyhirsch.moldynes.emulator;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class TestUtils {

  private TestUtils() {}

  static NesMapper loadMapper(String path) {
    try (InputStream input = Resources.getResource(path).openStream()) {
      return NesMapper.load(ByteBuffer.wrap(input.readAllBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static NesPpuPalette loadPalette(String path) {
    try (InputStream input = Resources.getResource(path).openStream()) {
      return NesPpuPalette.load(ByteBuffer.wrap(input.readAllBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static void run(String path) {
    NesMapper mapper = loadMapper(path);
    NesPpuPalette palette = loadPalette("Composite_wiki.pal");
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    try (SdlDisplay display = new SdlDisplay(joypad1, joypad2)) {
      NesBus bus = new NesBus(mapper, palette, display, joypad1, joypad2);
      while (!display.quit) {
        bus.tick();
      }
    }
  }
}
