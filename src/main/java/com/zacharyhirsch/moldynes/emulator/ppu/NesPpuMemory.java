package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;

final class NesPpuMemory {

  private final NesMapper mapper;
  private final byte[] palette;
  private final byte[] ram;

  NesPpuMemory(NesMapper mapper) {
    this.mapper = mapper;
    this.palette = new byte[0x20];
    this.ram = new byte[0x2000];
  }

  byte read(short address) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address < 0x3000) {
      return mapper.readPpu(address, ram);
    }
    if (0x3000 <= address && address < 0x3f00) {
      throw new InvalidAddressReadError(address);
    }
    if (0x3f00 <= address && address < 0x4000) {
      // TODO: buffer needs to be filled with actual ram, not palette ram.
      return palette[getPaletteAddress(address)];
    }
    throw new InvalidAddressReadError(address);
  }

  void write(short address, byte data) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x2fff) {
      mapper.writePpu(address, ram, data);
      return;
    }
    if (0x3000 <= address && address <= 0x3eff) {
      mapper.writePpu((short) (address & 0b1110_1111_1111_1111), ram, data);
      return;
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      palette[getPaletteAddress(address)] = data;
      return;
    }
    throw new InvalidAddressWriteError(address);
  }

  private int getPaletteAddress(short address) {
    int addr = address & 0b0000_0000_0001_1111;
    return switch (addr) {
      case 0x10, 0x14, 0x18, 0x1c -> addr - 0x10;
      default -> addr;
    };
  }
}
