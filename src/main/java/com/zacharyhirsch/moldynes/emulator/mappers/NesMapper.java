package com.zacharyhirsch.moldynes.emulator.mappers;

import java.nio.ByteBuffer;

public interface NesMapper {

  static NesMapper get(ByteBuffer buffer) {
    byte[] header = new byte[16];
    buffer.get(0, header);
    if (!new String(header, 0, 4).equals("NES\u001a")) {
      throw new IllegalArgumentException("bad magic string");
    }
    if ((header[6] & 0b0000_0100) == 0b0000_0100) {
      throw new IllegalArgumentException("trainer not implemented");
    }
    if ((header[7] & 0b0000_1100) == 0b0000_1000) {
      throw new IllegalArgumentException("NES 2.0 file format not implemented");
    }
    byte mapper = (byte) ((header[7] & 0b1111_0000) | ((header[6] & 0b1111_0000) >>> 4));
    return switch (mapper) {
      case 0x00 -> new NromNesMapper(header, buffer);
      case 0x01 -> new Mmc1NesMapper(header, buffer);
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  byte read(short address);

  void write(short address, byte data);

  byte readChr(short address);

  void writeChr(short address, byte data);

  boolean isVerticalMirroring();

  short getNametableMirrorAddress(short address);
}
