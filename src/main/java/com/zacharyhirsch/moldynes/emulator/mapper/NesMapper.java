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

  static short mirror(int address, boolean isVerticalMirroring) {
    int nametable = (address & 0b0000_1100_0000_0000) >>> 10;
    int offset =
        switch (nametable) {
          case 0 -> 0;
          case 1 -> isVerticalMirroring ? 1 : 0;
          case 2 -> isVerticalMirroring ? 0 : 1;
          case 3 -> 1;
          default -> throw new IllegalStateException();
        };
    return (short) ((offset << 10) | (address & 0b0000_0011_1111_1111));
  }

  final class UnknownMapperError extends RuntimeException {

    private UnknownMapperError(int mapper) {
      super("Unknown mapper %02x".formatted(mapper));
    }
  }
}
