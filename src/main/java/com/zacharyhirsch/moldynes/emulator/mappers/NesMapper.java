package com.zacharyhirsch.moldynes.emulator.mappers;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface NesMapper {

  static NesMapper load(String path) {
    try (FileInputStream input = new FileInputStream(path)) {
      return load(ByteBuffer.wrap(input.readAllBytes()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static NesMapper load(ByteBuffer buffer) {
    byte[] header = new byte[16];
    buffer.get(0, header);
    if (!(header[0] == 'N' && header[1] == 'E' && header[2] == 'S' && header[3] == 0x1a)) {
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
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  byte read(short address);

  void write(short address, byte data);

  short mirror(short address);
}
