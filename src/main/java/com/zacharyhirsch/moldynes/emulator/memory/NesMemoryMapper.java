package com.zacharyhirsch.moldynes.emulator.memory;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.nio.ByteBuffer;

public abstract class NesMemoryMapper {

  public static NesMemoryMapper get(ByteBuffer buffer) {
    byte[] header = new byte[16];
    buffer.get(0, header);
    if (!checkMagic(header)) {
      throw new IllegalArgumentException("bad magic string");
    }
    if ((header[7] & 0x0c) == 0x08) {
      throw new RuntimeException("NES 2.0 file format not implemented");
    }
    byte mapper = (byte) ((header[7] & 0b1111_0000) | ((header[6] & 0b1111_0000) >>> 4));
    return switch (mapper) {
      case 0x0 -> new NromNesMemoryMapper(header);
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  private static boolean checkMagic(byte[] header) {
    return new String(header, 0, 4).equals("NES\u001a");
  }

  public abstract NesPpu createPpu(ByteBuffer buffer, NesPpu.DrawFrame drawFrame);

  public abstract NesMemory createMem(ByteBuffer buffer, NesPpu ppu, NesApu apu);

  // https://www.nesdev.org/wiki/NROM
  private static final class NromNesMemoryMapper extends NesMemoryMapper {

    private final byte[] header;

    public NromNesMemoryMapper(byte[] header) {
      this.header = header;
    }

    @Override
    public NesPpu createPpu(ByteBuffer buffer, NesPpu.DrawFrame drawFrame) {
      int prgRomSize = header[4] << 14;
      if (prgRomSize != 0x4000 & prgRomSize != 0x8000) {
        throw new IllegalArgumentException(String.format("bad prg rom size: %04x", prgRomSize));
      }
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }

      int chrRomSize = header[5] << 13;
      byte[] chrRom = new byte[chrRomSize];
      buffer.get(0x10 + prgRomSize, chrRom, 0, chrRom.length);
      return new NesPpu(chrRom, drawFrame);
    }

    @Override
    public NesMemory createMem(ByteBuffer buffer, NesPpu ppu, NesApu apu) {
      int prgRomSize = header[4] << 14;
      if (prgRomSize != 0x4000 & prgRomSize != 0x8000) {
        throw new IllegalArgumentException(String.format("bad prg rom size: %04x", prgRomSize));
      }
      byte[] prgRom = new byte[prgRomSize];
      buffer.get(0x10, prgRom, 0, prgRom.length);
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }
      NesMemory.Builder builder =
          new NesMemory.Builder(ppu, apu)
              .put(0x6000, 0x2000, new NesRam(ByteBuffer.allocateDirect(0x2000)))
              .put(0x8000, prgRomSize, new NesRom(prgRom));
      if (prgRomSize == 0x4000) {
        builder.put(0xc000, 0x4000, new NesRom(prgRom));
      }
      return builder.build();
    }
  }
}
