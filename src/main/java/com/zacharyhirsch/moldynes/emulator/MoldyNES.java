package com.zacharyhirsch.moldynes.emulator;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.bus.NesBus;
import com.zacharyhirsch.moldynes.emulator.io.Io;
import com.zacharyhirsch.moldynes.emulator.io.SdlIo;
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
import java.lang.foreign.Arena;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class MoldyNES {

  private static final Logger log = LoggerFactory.getLogger(MoldyNES.class);

  static void main(String[] args) throws Exception {
    run(args[0]);
  }

  private static void run(String path) throws Exception {
    NesRom rom = readRom(path);
    ByteBuffer wram = readWram(rom, path + ".wram");
    NesPpuPalette palette = readPalette();

    NesMapper mapper = NesMapperFactory.load(rom, wram);

    try (Io io = new SdlIo(Arena.global())) {
      NesBus bus = new NesBus(mapper, palette, io);
      try {
        io.eventLoop().run(bus::tick);
      } catch (Exception e) {
        log.error("Emulator crashed!", e);
        io.video().setError(e);
        io.eventLoop().run(() -> {});
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
