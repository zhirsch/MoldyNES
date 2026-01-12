package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
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
      return Address.of(() -> (byte) 0, (_) -> {});
    }
    if (0x6000 <= address && address <= 0x7fff) {
      return Address.of(address - 0x6000, ram::get, ram::put);
    }
    if (0x8000 <= address && address <= 0xffff) {
      return Address.of(address, this::readPrgRom, (_, _) -> {});
    }
    throw new IllegalStateException();
  }

  @Override
  public Address resolvePpu(int address, ByteBuffer ppuRam) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x1fff) {
      return Address.of(address, this::readChr, this::writeChr);
    }
    if (0x2000 <= address && address <= 0x3eff) {
      return Address.of(mirror(address), ppuRam::get, ppuRam::put);
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      return Address.of(
          mirror(address),
          ppuRam::get,
          (_, _) -> {
            throw new InvalidWriteError(address);
          });
    }
    throw new IllegalStateException();
  }

  private byte readChr(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    return rom.chr().get(address & (rom.chr().capacity() - 1));
  }

  private void writeChr(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    rom.chr().put(address & (rom.chr().capacity() - 1), data);
  }

  private Byte readPrgRom(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    return rom.prg().get(address & (rom.prg().capacity() - 1));
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
    return NesMapper.mirror(address, isVerticalMirroring);
  }
}
