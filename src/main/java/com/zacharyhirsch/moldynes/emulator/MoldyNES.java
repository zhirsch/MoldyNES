package com.zacharyhirsch.moldynes.emulator;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.io.SdlDisplay;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomLoader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class MoldyNES {

  private static final Logger log = LoggerFactory.getLogger(MoldyNES.class);

  static void main(String[] args) throws IOException {
    run(args[0]);
  }

  private static void run(String path) throws IOException {
    NesRom rom;
    try (InputStream input = new FileInputStream(path)) {
      rom = NesRomLoader.load(input);
    }
    NesPpuPalette palette;
    try (InputStream input = Resources.getResource("Composite_wiki.pal").openStream()) {
      palette = NesPpuPalette.load(input);
    }

    NesMapper mapper = NesMapper.load(rom);
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    try (SdlDisplay display = new SdlDisplay(joypad1, joypad2)) {
      NesBus bus = new NesBus(mapper, palette, display, joypad1, joypad2);
      try {
        while (!display.quit) {
          bus.tick();
        }
      } catch (Exception e) {
        log.error("Emulator crashed!", e);
        display.setError();
        while (!display.quit) {
          display.pump();
        }
      }
    }
  }
}
