package com.zacharyhirsch.moldynes.emulator.mappers;

import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/NROM
final class NromNesMapper implements NesMapper {

  private final byte[] header;
  private final byte[] ram;
  private final byte[] prgRom;
  private final byte[] chrRam;

  public NromNesMapper(byte[] header, ByteBuffer buffer) {
    this.header = header;
    ram = new byte[0x2000];

    prgRom = new byte[header[4] << 14];
    buffer.get(0x10, prgRom, 0, prgRom.length);

    int chrRomLength = header[5] << 13;
    int chrRamLength = Math.max(chrRomLength, 0x2000);
    chrRam = new byte[chrRamLength];
    buffer.get(0x10 + prgRom.length, chrRam, 0, chrRomLength);
  }

  @Override
  public byte read(short address) {
    int addr = Short.toUnsignedInt(address);
    if (0x6000 <= addr && addr < 0x8000) {
      return ram[addr - 0x6000];
    }
    if (0x8000 <= addr && addr < 0x10000) {
      addr -= 0x8000;
      if (prgRom.length == 0x4000) {
        addr %= 0x4000;
      }
      return prgRom[addr];
    }
    return (byte) 0xff;
  }

  @Override
  public void write(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    if (0x6000 <= addr && addr < 0x8000) {
      ram[addr - 0x6000] = data;
      return;
    }
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
  }

  @Override
  public byte readChr(short address) {
    return chrRam[Short.toUnsignedInt(address)];
  }

  @Override
  public void writeChr(short address, byte data) {
    chrRam[Short.toUnsignedInt(address)] = data;
  }

  @Override
  public boolean isVerticalMirroring() {
    return (header[6] & 1) == 1;
  }

  @Override
  public short getNametableMirrorAddress(short address) {
    // Horizontal mirroring:
    //   [ A ] [ a ]
    //   [ B ] [ b ]
    //
    // Vertical mirroring:
    //   [ A ] [ B ]
    //   [ a ] [ b ]

    int nametable = (address & 0b0000_1100_0000_0000) >>> 10;

    short offset;
    if (isVerticalMirroring()) {
      offset =
          switch (nametable) {
            case 0, 2 -> 0b0000_0000_0000_0000;
            case 1, 3 -> 0b0000_0100_0000_0000;
            default -> throw new IllegalStateException();
          };
    } else {
      offset =
          switch (nametable) {
            case 0, 1 -> 0b0000_0000_0000_0000;
            case 2, 3 -> 0b0000_0100_0000_0000;
            default -> throw new IllegalStateException();
          };
    }

    int index = address & 0b0000_0011_1111_1111;
    return (short) (offset | index);
  }
}
