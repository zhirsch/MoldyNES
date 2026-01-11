package com.zacharyhirsch.moldynes.emulator.rom;

import com.zacharyhirsch.moldynes.emulator.rom.NesRomProperties.NametableLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public final class NesRomLoader {

  private NesRomLoader() {}

  public static NesRom load(String path) {
    ByteBuffer buffer;
    try (FileInputStream input = new FileInputStream(path)) {
      buffer = ByteBuffer.wrap(input.readAllBytes());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    NesRom rom = load(buffer);
    if (buffer.hasRemaining()) {
      throw new IllegalStateException("buffer not empty");
    }
    return rom;
  }

  private static NesRom load(ByteBuffer buffer) {
    byte[] header = read(buffer, 16);
    if (!(header[0] == 'N' && header[1] == 'E' && header[2] == 'S' && header[3] == 0x1a)) {
      throw new IllegalArgumentException("bad magic string");
    }
    if ((header[7] & 0b0000_1100) == 0b0000_1000) {
      return load20(header, buffer);
    }
    return load10(header, buffer);
  }

  private static NesRom load20(byte[] header, ByteBuffer buffer) {
    byte[] prgRom = read(buffer, getSectionSize(header[4], (byte) (header[9] & 0b0000_1111), 14));
    byte[] chrRom = read(buffer, getSectionSize(header[5], (byte) (header[9] & 0b1111_0000), 13));
    if ((header[6] & 0b0000_0100) == 0b0000_0100) {
      throw new IllegalArgumentException("trainer not implemented");
    }
    if ((header[7] & 0b0000_0011) != 0) {
      throw new IllegalArgumentException("bad console type %02x".formatted(header[7]));
    }
    if ((header[8] & 0b1111_0000) >>> 4 != 0) {
      throw new IllegalArgumentException("bad submapper: %d".formatted(header[8]));
    }
    if (header[10] != 0) {
      throw new IllegalArgumentException("bad prg-ram/eeprom size: %02x".formatted(header[10]));
    }
    if (header[11] != 0) {
      throw new IllegalArgumentException("bad chr-ram size: %02x".formatted(header[11]));
    }
    if (header[12] != 0) {
      throw new IllegalArgumentException("bad cpu/ppu timing: %02x".formatted(header[12]));
    }
    if (header[13] != 0) {
      throw new IllegalArgumentException("bad byte 13: %02x".formatted(header[12]));
    }
    if (header[14] != 0) {
      throw new IllegalArgumentException("bad miscellaneous roms: %02x".formatted(header[14]));
    }
    if (header[15] != 1) {
      throw new IllegalArgumentException("bad expansion devices: %02x".formatted(header[15]));
    }

    int mapper = getMapper(header[6], header[7], header[8]);
    NametableLayout nametableLayout = NametableLayout.fromHeader6(header[6]);
    return new NesRom(
        new NesRomSection(0x4000, ByteBuffer.wrap(prgRom).asReadOnlyBuffer()),
        new NesRomSection(0x2000, ByteBuffer.wrap(chrRom).asReadOnlyBuffer()),
        new NesRomProperties(mapper, nametableLayout));
  }

  private static NesRom load10(byte[] header, ByteBuffer buffer) {
    byte[] prgRom = read(buffer, header[4] << 14);
    ByteBuffer chrRom;
    byte[] chrRomTemp = read(buffer, header[5] << 13);
    if (chrRomTemp.length == 0) {
      chrRom = ByteBuffer.wrap(new byte[0x2000]);
    } else {
      chrRom = ByteBuffer.wrap(chrRomTemp).asReadOnlyBuffer();
    }
    if ((header[6] & 0b0000_0100) == 0b0000_0100) {
      throw new IllegalArgumentException("trainer not implemented");
    }
    if ((header[7] & 0b0000_0011) != 0) {
      throw new IllegalArgumentException("bad console type %02x".formatted(header[7]));
    }
    if (header[8] != 0) {
      throw new IllegalArgumentException("bad prg-ram size: %02x".formatted(header[8]));
    }
    if ((header[9] & 0b0000_0001) != 0) {
      throw new IllegalArgumentException("bad tv system: %02x".formatted(header[9]));
    }
    if (header[10] != 0) {
      throw new IllegalArgumentException("bad byte 10: %02x".formatted(header[10]));
    }
    if (header[11] != 0) {
      throw new IllegalArgumentException("bad byte 11: %02x".formatted(header[11]));
    }
    if (header[12] != 0) {
      throw new IllegalArgumentException("bad byte 12: %02x".formatted(header[12]));
    }
    if (header[13] != 0) {
      throw new IllegalArgumentException("bad byte 13: %02x".formatted(header[12]));
    }
    if (header[14] != 0) {
      throw new IllegalArgumentException("bad byte 14: %02x".formatted(header[14]));
    }
    if (header[15] != 0) {
      throw new IllegalArgumentException("bad byte 15: %02x".formatted(header[15]));
    }

    int mapper = getMapper(header[6], header[7], (byte) 0);
    NametableLayout nametableLayout = NametableLayout.fromHeader6(header[6]);
    return new NesRom(
        new NesRomSection(0x4000, ByteBuffer.wrap(prgRom).asReadOnlyBuffer()),
        new NesRomSection(0x2000, chrRom),
        new NesRomProperties(mapper, nametableLayout));
  }

  private static byte[] read(ByteBuffer buffer, int size) {
    byte[] array = new byte[size];
    buffer.get(array);
    return array;
  }

  private static int getSectionSize(byte lsb, byte msb, int blockSizeLog2) {
    if (msb == 0xf) {
      int m = (lsb & 0b0000_0011) >>> 0;
      int e = (lsb & 0b1111_1100) >>> 2;
      long size = Math.powExact(2L, e) * (2 * m + 1);
      if (size > Integer.MAX_VALUE) {
        // TODO: allow larger sizes. Java limits arrays to 32-bits.
        throw new IllegalArgumentException("Section size too large: %08x".formatted(size));
      }
      return (int) size;
    }
    return ((msb << 8) | lsb) * (1 << blockSizeLog2);
  }

  private static int getMapper(byte header6, byte header7, byte header8) {
    int nibble1 = (header6 & 0b1111_0000) >>> 4;
    int nibble2 = (header7 & 0b1111_0000) >>> 4;
    int nibble3 = (header8 & 0b0000_1111) >>> 0;
    return (nibble3 << 8) | (nibble2 << 4) | nibble1;
  }
}
