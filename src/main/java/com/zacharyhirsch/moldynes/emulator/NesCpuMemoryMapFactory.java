package com.zacharyhirsch.moldynes.emulator;

import java.nio.ByteBuffer;

public abstract class NesCpuMemoryMapFactory {

  public static NesCpuMemoryMapFactory get(int mapper) {
    return switch (mapper) {
      case 0x0 -> new NromNesCpuMemoryMap();
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  public abstract NesCpuMemoryMap load(byte[] header, ByteBuffer buffer);

  // https://www.nesdev.org/wiki/NROM
  private static final class NromNesCpuMemoryMap extends NesCpuMemoryMapFactory {

    @Override
    public NesCpuMemoryMap load(byte[] header, ByteBuffer buffer) {
      ByteBuffer prgRom = getPrgRom(header, buffer);
      int chrRomSize = header[5] << 13;
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }
      return new NesCpuMemoryMap.Builder()
          .put(0x8000, 0x4000, new NesRom(prgRom))
          .put(0xc000, 0x4000, new NesRom(prgRom))
          .build();
    }

    private static ByteBuffer getPrgRom(byte[] header, ByteBuffer buffer) {
      int prgRomSize = header[4] << 14;
      if (prgRomSize != 0x4000) {
        throw new IllegalArgumentException(String.format("bad prg rom size: %04x", prgRomSize));
      }
      return buffer.slice(16, prgRomSize).asReadOnlyBuffer();
    }
  }
}
