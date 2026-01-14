package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.memory.Address;
import java.nio.ByteBuffer;

final class NesPpuMemory {

  private final NesMapper mapper;
  private final ByteBuffer palette;

  NesPpuMemory(NesMapper mapper) {
    this.mapper = mapper;
    this.palette = ByteBuffer.wrap(new byte[0x20]);
  }

  byte read(short address) {
    return resolve(address).read();
  }

  void write(short address, byte data) {
    resolve(address).write(data);
  }

  private Address resolve(int address) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x3eff) {
      return mapper.resolvePpu(address);
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      // TODO: When read, buffer needs to be filled with actual ram, not palette ram.
      return Address.of(getPaletteAddress(address), palette::get, palette::put);
    }
    throw new IllegalStateException();
  }

  private int getPaletteAddress(int address) {
    int addr = address & 0b0000_0000_0001_1111;
    return switch (addr) {
      case 0x10, 0x14, 0x18, 0x1c -> addr - 0x10;
      default -> addr;
    };
  }
}
