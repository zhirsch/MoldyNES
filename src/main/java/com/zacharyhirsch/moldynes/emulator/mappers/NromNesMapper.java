package com.zacharyhirsch.moldynes.emulator.mappers;

import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/NROM
final class NromNesMapper implements NesMapper {

  private final byte[] header;
  private final byte[] ram;
  private final byte[] prgRom;
  private final byte[] chrRom;

  public NromNesMapper(byte[] header, ByteBuffer buffer) {
    this.header = header;
    ram = new byte[0x2000];

    prgRom = new byte[header[4] << 14];
    buffer.get(0x10, prgRom, 0, prgRom.length);

    chrRom = new byte[header[5] << 13];
    buffer.get(0x10 + prgRom.length, chrRom, 0, chrRom.length);
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
    //    throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
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
    int addr = Short.toUnsignedInt(address);
    return chrRom[addr];
  }

  @Override
  public void writeChr(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
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
    //

    boolean vertical = isVerticalMirroring();

    short addr = (short) (address & 0b0010_1111_1111_1111);
    short ramIndex = (short) (addr - 0x2000);
    int nametable = ramIndex / 0x400;
    if (vertical && (nametable == 2 || nametable == 3)) {
      return (short) (ramIndex - 0x800);
    }
    if (!vertical && (nametable == 1 || nametable == 2)) {
      return (short) (ramIndex - 0x400);
    }
    if (!vertical && nametable == 3) {
      return (short) (ramIndex - 0x800);
    }
    return ramIndex;
  }
}
