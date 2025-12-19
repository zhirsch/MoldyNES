package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.rom.NesRom;

public interface NesMapper {

  static NesMapper load(NesRom rom) {
    return switch (rom.properties().mapper()) {
      case 0x00 -> new NromNesMapper(rom);
      default -> throw new UnknownMapperError(rom.properties().mapper());
    };
  }

  byte read(short address, byte[] ppuRam);

  void write(short address, byte[] ppuRam, byte data);

  final class UnknownMapperError extends RuntimeException {

    private UnknownMapperError(int mapper) {
      super("Unknown mapper %02x".formatted(mapper));
    }
  }
}
