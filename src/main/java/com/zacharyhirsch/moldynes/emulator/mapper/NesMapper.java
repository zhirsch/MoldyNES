package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface NesMapper {

  Logger log = LoggerFactory.getLogger(NesMapper.class);

  static NesMapper load(NesRom rom) {
    log.info("Using mapper {}", "%02x".formatted(rom.properties().mapper()));
    return switch (rom.properties().mapper()) {
      case 0x00 -> new NromNesMapper(rom);
      case 0x01 -> new Mmc1NesMapper(rom);
      default -> throw new UnknownMapperError(rom.properties().mapper());
    };
  }

  Address resolveCpu(int address);

  Address resolvePpu(int address, ByteBuffer ppuRam);

  final class UnknownMapperError extends RuntimeException {

    private UnknownMapperError(int mapper) {
      super("Unknown mapper %02x".formatted(mapper));
    }
  }
}
