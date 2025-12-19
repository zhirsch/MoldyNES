package com.zacharyhirsch.moldynes.emulator;

import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.io.SdlDisplay;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomLoader;

final class MoldyNES {

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
      while (!display.quit) {
        bus.tick();
      }
    }
  }
}
