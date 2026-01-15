package com.zacharyhirsch.moldynes.emulator.rom;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class NesRomLoader {
  
  private NesRomLoader() {}

  public static NesRom load(InputStream input) throws IOException {
    byte[] header = input.readNBytes(16);
    if (!isMagic(header)) {
      throw new IllegalArgumentException("bad magic string");
    }
    return isNes20(header) ? load20(header, input) : load10(header, input);
  }

  private static NesRom load20(byte[] header, InputStream input) throws IOException {
    int prgSize = getSectionSize(header[4], (byte) (header[9] & 0b0000_1111), 0x4000);
    int chrSize = getSectionSize(header[5], (byte) (header[9] & 0b1111_0000), 0x2000);
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
    boolean battery = (header[6] &0b0000_0010) != 0;
    return new NesRom(
        ByteBuffer.wrap(input.readNBytes(prgSize)).asReadOnlyBuffer(),
        ByteBuffer.wrap(input.readNBytes(chrSize)).asReadOnlyBuffer(),
        new NesRomProperties(mapper, nametableLayout, battery));
  }

  private static NesRom load10(byte[] header, InputStream input) throws IOException {
    int prgSize = getSectionSize(header[4], (byte) 0, 0x4000);
    int chrSize = getSectionSize(header[5], (byte) 0, 0x2000);
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
    boolean battery = (header[6] &0b0000_0010) != 0;
    return new NesRom(
        ByteBuffer.wrap(input.readNBytes(prgSize)).asReadOnlyBuffer(),
        chrSize == 0
            ? ByteBuffer.wrap(new byte[0x2000])
            : ByteBuffer.wrap(input.readNBytes(chrSize)).asReadOnlyBuffer(),
        new NesRomProperties(mapper, nametableLayout, battery));
  }

  private static int getSectionSize(byte lsb, byte msb, int blockSize) {
    if (msb == 0xf) {
      int m = (lsb & 0b0000_0011) >>> 0;
      int e = (lsb & 0b1111_1100) >>> 2;
      return Math.unsignedPowExact(2, e) * (2 * m + 1);
    }
    return ((msb << 8) | lsb) * blockSize;
  }

  private static int getMapper(byte header6, byte header7, byte header8) {
    int nibble1 = (header6 & 0b1111_0000) >>> 4;
    int nibble2 = (header7 & 0b1111_0000) >>> 4;
    int nibble3 = (header8 & 0b0000_1111) >>> 0;
    return (nibble3 << 8) | (nibble2 << 4) | nibble1;
  }

  private static boolean isMagic(byte[] header) {
    return header[0] == 'N' && header[1] == 'E' && header[2] == 'S' && header[3] == 0x1a;
  }

  private static boolean isNes20(byte[] header) {
    return (header[7] & 0b0000_1100) == 0b0000_1000;
  }
}
