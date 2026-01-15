package com.zacharyhirsch.moldynes.emulator;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.bus.NesBus;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.io.SdlDisplay;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapperFactory;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class MoldyNES {

  private static final Logger log = LoggerFactory.getLogger(MoldyNES.class);

  static void main(String[] args) throws IOException {
    run(args[0]);
  }

  private static void run(String path) throws IOException {
    NesRom rom = readRom(path);
    ByteBuffer wram = readWram(rom, path + ".wram");
    NesPpuPalette palette = readPalette();

    NesMapper mapper = NesMapperFactory.load(rom, wram);
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
        throw e;
      }
      writeWram(rom, path + ".wram", wram);
    }
  }

  private static NesRom readRom(String path) throws IOException {
    try (InputStream input = new FileInputStream(path)) {
      return NesRomLoader.load(input);
    }
  }

  private static ByteBuffer readWram(NesRom rom, String path) throws IOException {
    if (!rom.properties().battery()) {
      return ByteBuffer.wrap(new byte[0x2000]);
    }
    File file = new File(path);
    if (!file.exists()) {
      return ByteBuffer.wrap(new byte[0x2000]);
    }
    try (InputStream input = new FileInputStream(file)) {
      return ByteBuffer.wrap(input.readAllBytes());
    }
  }

  private static void writeWram(NesRom rom, String path, ByteBuffer wram) throws IOException {
    if (!rom.properties().battery()) {
      return;
    }
    try (OutputStream output = new FileOutputStream(path)) {
      output.write(wram.array());
    }
  }

  private static NesPpuPalette readPalette() throws IOException {
    try (InputStream input = Resources.getResource("Composite_wiki.pal").openStream()) {
      return NesPpuPalette.load(input);
    }
  }
}
