package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.io.SdlDisplay;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class MoldyNES {

  private static final Logger log = LoggerFactory.getLogger(MoldyNES.class);

  static void main(String[] args) {
    run(args[0]);
  }

  private static void run(String path) {
    NesRom rom = NesRomLoader.load(path);
    NesMapper mapper = NesMapper.load(rom);
    NesPpuPalette palette = NesPpuPalette.load("Composite_wiki.pal");
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
        while (!display.quit) {
          sleep();
          display.pump();
        }
      }
    }
  }

  private static void sleep() {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ex) {
      throw new RuntimeException(ex);
    }
  }
}
