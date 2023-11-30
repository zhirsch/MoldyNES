package com.zacharyhirsch.moldynes.emulator.mappers;

import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/MMC1#iNES_Mapper_001
final class Mmc1NesMapper implements NesMapper {

  private final byte[] ram;
  private final byte[][] prgRomBanks;
  private final byte[][] chrRomBanks;
  private final byte[] chrRam = new byte[0x2000];

  private byte load = 0b10000;
  private byte control = 0b01100;
  private byte chrBank0 = 0;
  private byte chrBank1 = 0;
  private byte prgBank = 0;

  public Mmc1NesMapper(byte[] header, ByteBuffer buffer) {
    ram = new byte[0x2000];

    int offset = 0x10;

    prgRomBanks = new byte[header[4]][];
    offset = readBank(prgRomBanks, 0x4000, buffer, offset);

    chrRomBanks = new byte[header[5]][];
    readBank(chrRomBanks, 0x2000, buffer, offset);
  }

  private int readBank(byte[][] banks, int bankLength, ByteBuffer buffer, int offset) {
    for (int i = 0; i < banks.length; i++) {
      byte[] bank = banks[i] = new byte[bankLength];
      buffer.get(offset, bank, 0, bank.length);
      offset += bank.length;
    }
    return offset;
  }

  @Override
  public byte read(short address) {
    int addr = Short.toUnsignedInt(address);
    if (0x6000 <= addr && addr < 0x8000) {
      addr -= 0x6000;
      return ram[addr];
    }
    byte[] bank = getPrgRomBank(addr);
    if (0x8000 <= addr && addr < 0xc000) {
      return bank[addr - 0x8000];
    }
    if (0xc000 <= addr && addr < 0x10000) {
      return bank[addr - 0xc000];
    }
    throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
  }

  @Override
  public void write(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    if (0x6000 <= addr && addr < 0x8000) {
      ram[addr - 0x6000] = data;
      return;
    }
    if (0x8000 <= addr && addr < 0x10000) {
      writeLoadRegister(addr, data);
      return;
    }
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
  }

  private void writeLoadRegister(int addr, byte data) {
    if (bit(data, 7) == 1) {
      resetLoadRegister();
    } else {
      shiftLoadRegister(addr, bit(data, 0));
    }
  }

  private void shiftLoadRegister(int addr, byte bit) {
    byte last = (byte) (load & 1);
    load = (byte) (bit << 4 | (load >>> 1));
    if (last == 1) {
      flushLoadRegister(addr);
    }
  }

  private void flushLoadRegister(int addr) {
    if (0x8000 <= addr && addr < 0xa000) {
      control = load;
    } else if (0xa000 <= addr && addr < 0xc000) {
      chrBank0 = load;
    } else if (0xc000 <= addr && addr < 0xe000) {
      chrBank1 = load;
    } else if (0xe000 <= addr && addr < 0x10000) {
      prgBank = load;
    }
    load = 0b10000;
  }

  private void resetLoadRegister() {
    control = (byte) ((control & 0b10000) | 0b01100);
    load = 0b10000;
  }

  @Override
  public byte readChr(short address) {
    int addr = Short.toUnsignedInt(address);
    if (bit(control, 4) == 0) {
      return chrRam[addr];
    }
    throw new IllegalArgumentException(String.format("unable to read address %04x", addr));
  }

  @Override
  public void writeChr(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    if (bit(control, 4) == 0) {
      chrRam[addr] = data;
      return;
    }
    throw new IllegalArgumentException(String.format("unable to write address %04x", addr));
  }

  @Override
  public boolean isVerticalMirroring() {
    if (bit(control, 1) == 0) {
      throw new IllegalStateException("MMC1: one-screen mirroring not implemented");
    }
    return bit(control, 0) == 0;
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

  private byte[] getPrgRomBank(int addr) {
    int mode = (control & 0b01100) >>> 2;
    return switch (mode) {
      case 0, 1 -> get32kPrgRomBank(addr);
      case 2, 3 -> get16kPrgRomBank(addr, mode);
      default -> throw new IllegalStateException(String.valueOf(mode));
    };
  }

  private byte[] get32kPrgRomBank(int addr) {
    // Switch 32 kB at 0x8000
    if (0x8000 <= addr && addr < 0xc000) {
      return prgRomBanks[prgBank & 0b01110];
    }
    if (0xc000 <= addr && addr < 0x10000) {
      return prgRomBanks[(prgBank & 0b01110) + 1];
    }
    throw new IllegalStateException();
  }

  private byte[] get16kPrgRomBank(int addr, int mode) {
    // Fix one bank, switch the other.
    if (0x8000 <= addr && addr < 0xc000) {
      return mode == 2 ? prgRomBanks[0] : prgRomBanks[prgBank & 0b01111];
    }
    if (0xc000 <= addr && addr < 0x10000) {
      return mode == 2 ? prgRomBanks[prgBank & 0b01111] : prgRomBanks[prgRomBanks.length - 1];
    }
    throw new IllegalStateException();
  }

  private static byte bit(byte value, int i) {
    return (byte) ((Byte.toUnsignedInt(value) >>> i) & 1);
  }
}
