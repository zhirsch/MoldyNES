package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;

public interface NesMapper {

  static NesMapper load(NesRom rom) {
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
