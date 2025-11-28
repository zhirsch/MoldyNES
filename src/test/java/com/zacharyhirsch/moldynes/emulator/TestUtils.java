package com.zacharyhirsch.moldynes.emulator;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

final class TestUtils {

  private TestUtils() {}

  static ByteBuffer read(String path) throws IOException {
    try (InputStream is = Resources.getResource(path).openStream()) {
      return ByteBuffer.wrap(is.readAllBytes());
    }
  }

  static NesPpuPalette loadPalette() throws IOException {
    try (InputStream input = Resources.getResource("Composite_wiki.pal").openStream()) {
      return NesPpuPalette.load(input);
    }
  }

  static void run(String path) throws IOException {
    ByteBuffer buffer = read(path);
    NesMapper mapper = NesMapper.get(buffer);
    NesApu apu = new NesApu();
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    try (SdlDisplay display = new SdlDisplay(joypad1, joypad2)) {
      NesPpu ppu = new NesPpu(mapper, display, loadPalette());
      NesBus bus = new NesBus(mapper, ppu, joypad1, joypad2);
      NesCpu cpu = new NesCpu(ppu, bus);

      Emulator emulator = new Emulator(cpu, ppu, apu);
      while (!display.quit) {
        emulator.step();
      }
    }
  }
}
