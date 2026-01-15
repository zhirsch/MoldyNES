package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NesMapperFactory {

  private static final Logger log = LoggerFactory.getLogger(NesMapperFactory.class);

  public static NesMapper load(NesRom rom, ByteBuffer wram) {
    log.info("Using mapper {}", "%02x".formatted(rom.properties().mapper()));
    return switch (rom.properties().mapper()) {
      case 0x00 -> new NromNesMapper(rom);
      case 0x01 -> new Mmc1NesMapper(rom, wram);
      case 0x02 -> new UxRomNesMapper(rom);
      case 0x03 -> new CnromNesMapper(rom);
      case 0x04 -> new Mmc3NesMapper(rom);
      default -> throw new UnknownMapperError(rom.properties().mapper());
    };
  }

  private static final class UnknownMapperError extends RuntimeException {

    private UnknownMapperError(int mapper) {
      super("Unknown mapper %02x".formatted(mapper));
    }
  }
}
