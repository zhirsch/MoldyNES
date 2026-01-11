package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomProperties.NametableLayout;
import java.nio.ByteBuffer;

// https://www.nesdev.org/wiki/NROM
final class NromNesMapper implements NesMapper {

  private final NesRom rom;
  private final ByteBuffer ram;

  NromNesMapper(NesRom rom) {
    this.rom = rom;
    this.ram = ByteBuffer.wrap(new byte[0x2000]);
  }

  @Override
  public Address resolveCpu(int address) {
    assert 0x0000 <= address && address <= 0xffff;
    if (0x0000 <= address && address <= 0x5fff) {
      return Address.of(address, InvalidReadError::throw_, InvalidWriteError::throw_);
    }
    if (0x6000 <= address && address <= 0x7fff) {
      return Address.of(address - 0x6000, ram::get, ram::put);
    }
    if (0x8000 <= address && address <= 0xffff) {
      return Address.of(address, this::readPrgRom, InvalidWriteError::throw_);
    }
    throw new IllegalStateException();
  }

  @Override
  public Address resolvePpu(int address, ByteBuffer ppuRam) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x1fff) {
      return Address.of(address, this::readChrRom, InvalidWriteError::throw_);
    }
    if (0x2000 <= address && address <= 0x3eff) {
      return Address.of(mirror(address), ppuRam::get, ppuRam::put);
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      return Address.of(mirror(address), ppuRam::get, InvalidWriteError::throw_);
    }
    throw new IllegalStateException();
  }

  private byte readChrRom(int address) {
    return rom.chr().read(address, 0);
  }

  private Byte readPrgRom(int address) {
    if (rom.prg().value().capacity() == 0x4000) {
      return rom.prg().read(address - 0x8000, 0, 0);
    } else {
      return rom.prg().read(address - 0x8000, 0);
    }
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
