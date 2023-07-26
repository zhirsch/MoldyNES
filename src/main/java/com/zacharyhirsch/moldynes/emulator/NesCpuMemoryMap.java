package com.zacharyhirsch.moldynes.emulator;

import java.nio.ByteBuffer;

public abstract class NesCpuMemoryMap {

  public static NesCpuMemoryMap get(int mapper) {
    return switch (mapper) {
      case 0x0 -> new NromNesCpuMemoryMap();
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  public abstract NesCpuMemory load(byte[] header, ByteBuffer buffer);

  // https://www.nesdev.org/wiki/NROM
  private static final class NromNesCpuMemoryMap extends NesCpuMemoryMap {

    @Override
    public NesCpuMemory load(byte[] header, ByteBuffer buffer) {
      int prgRomSize = header[4] << 14;
      if (prgRomSize != 0x4000) {
        throw new IllegalArgumentException(String.format("bad prg rom size: %04x", prgRomSize));
      }
      int chrRomSize = header[5] << 13;
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }
      return new NesCpuMemory.Builder()
          // .rom(0x8000, 0x4000, buffer.slice(16, prgRomSize).asReadOnlyBuffer())
          .rom(0x8000, 0x4000, buffer.slice(16, prgRomSize))
          .mirror(0xc000, 0x4000, 0x8000)
          .build();
    }
  }
}
