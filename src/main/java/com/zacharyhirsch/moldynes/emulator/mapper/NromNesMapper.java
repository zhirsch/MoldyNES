package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomProperties.NametableLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.nesdev.org/wiki/NROM
final class NromNesMapper implements NesMapper {

  private static final Logger log = LoggerFactory.getLogger(NromNesMapper.class);

  private final NesRom rom;
  private final byte[] ram;

  NromNesMapper(NesRom rom) {
    this.rom = rom;
    this.ram = new byte[0x2000];
  }

  @Override
  public byte readCpu(short address) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x5fff) {
      throw new InvalidAddressReadError(address);
    }
    if (0x6000 <= addr && addr <= 0x7fff) {
      return ram[addr - 0x6000];
    }
    if (0x8000 <= addr && addr <= 0xffff) {
      if (rom.prg().value().length == 0x4000) {
        return rom.prg().read(addr - 0x8000, 0, 0);
      } else {
        return rom.prg().read(addr - 0x8000, 0);
      }
    }
    throw new InvalidAddressReadError(address);
  }

  @Override
  public byte readPpu(short address, byte[] ppuRam) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0x3fff;
    if (0x0000 <= addr && addr <= 0x1fff) {
      return rom.chr().read(addr, 0);
    }
    if (0x2000 <= addr && addr <= 0x3fff) {
      return ppuRam[mirror(addr)];
    }
    throw new InvalidAddressReadError(address);
  }

  @Override
  public void writeCpu(short address, byte data) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0xffff;
    if (0x0000 <= addr && addr <= 0x5fff) {
      throw new InvalidAddressWriteError(address);
    }
    if (0x6000 <= addr && addr <= 0x7fff) {
      ram[addr - 0x6000] = data;
      return;
    }
    if (0x8000 <= addr && addr <= 0xffff) {
      throw new InvalidAddressWriteError(address);
    }
    throw new InvalidAddressWriteError(address);
  }

  @Override
  public void writePpu(short address, byte[] ppuRam, byte data) {
    int addr = Short.toUnsignedInt(address);
    assert 0x0000 <= addr && addr <= 0x3fff;
    if (0x0000 <= addr && addr <= 0x1fff) {
      throw new InvalidAddressWriteError(address);
    }
    if (0x2000 <= addr && addr <= 0x3eff) {
      ppuRam[mirror(addr)] = data;
      return;
    }
    if (0x3f00 <= addr && addr <= 0x3fff) {
      throw new InvalidAddressWriteError(address);
    }
    throw new InvalidAddressWriteError(address);
  }

  private short mirror(int address) {
    // Horizontal mirroring:
    //   [ A ] [ a ]
    //   [ B ] [ b ]
    //
    // Vertical mirroring:
    //   [ A ] [ B ]
    //   [ a ] [ b ]

    boolean isVerticalMirroring = rom.properties().nametableLayout() == NametableLayout.VERTICAL;
    int nametable = (address & 0b0000_1100_0000_0000) >>> 10;
    int offset =
        switch (nametable) {
          case 0 -> 0;
          case 1 -> isVerticalMirroring ? 1 : 0;
          case 2 -> isVerticalMirroring ? 0 : 1;
          case 3 -> 1;
          default -> throw new IllegalStateException();
        };
    int index = address & 0b0000_0011_1111_1111;
    return (short) ((offset << 10) | index);
  }
}
