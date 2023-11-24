package com.zacharyhirsch.moldynes.emulator;

import java.nio.ByteBuffer;

public abstract class NesCpuMemoryMapFactory {

  public static NesCpuMemoryMapFactory get(int mapper) {
    return switch (mapper) {
      case 0x0 -> new NromNesCpuMemoryMap();
//      case 0x1 -> new Mmc1NesCpuMemoryMap();
      default -> throw new IllegalArgumentException("mapper unimplemented: " + mapper);
    };
  }

  public abstract NesCpuMemoryMap load(byte[] header, ByteBuffer buffer);

  // https://www.nesdev.org/wiki/NROM
  private static final class NromNesCpuMemoryMap extends NesCpuMemoryMapFactory {

    @Override
    public NesCpuMemoryMap load(byte[] header, ByteBuffer buffer) {
      int prgRomSize = header[4] << 14;
      ByteBuffer prgRom = buffer.slice(16, prgRomSize).asReadOnlyBuffer();
      int chrRomSize = header[5] << 13;
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }
      NesCpuMemoryMap.Builder builder = new NesCpuMemoryMap.Builder();
      // PRG RAM - mainly used for test debug output?
      builder.put(0x6000, 0x2000, new NesRam(ByteBuffer.allocateDirect(0x2000)));
      if (prgRomSize == 0x4000) {
        builder.put(0x8000, 0x4000, new NesRom(prgRom)).put(0xc000, 0x4000, new NesRom(prgRom));
      } else if (prgRomSize == 0x8000) {
        builder.put(0x8000, 0x8000, new NesRom(prgRom));
      } else {
        throw new IllegalArgumentException(String.format("bad prg rom size: %04x", prgRomSize));
      }
      return builder.build();
    }
  }

  private static class Mmc1NesCpuMemoryMap extends NesCpuMemoryMapFactory {

    @Override
    public NesCpuMemoryMap load(byte[] header, ByteBuffer buffer) {
      int prgRomSize = header[4] << 14;
      ByteBuffer prgRom = buffer.slice(16, prgRomSize).asReadOnlyBuffer();
      int chrRomSize = header[5] << 13;
      if ((header[6] & 0b0000_0100) == 0b0000_0100) {
        throw new IllegalArgumentException("trainer not implemented");
      }
      return new NesCpuMemoryMap.Builder()
          .put(0x6000, 0x2000, new NesRam(ByteBuffer.allocateDirect(0x2000)))
          .put(0x8000, 0x8000, new Rom(prgRom))
          //          .put(0x8000, 0x4000, new Rom(prgRom.slice(0x0000, 0x4000)))
          //          .put(0xc000, 0x4000, new Rom(prgRom.slice(0xc000, 0x4000)))
          .build();
    }

    private final class Rom implements NesDevice {

      private final ByteBuffer rom;

      private short shift;
      private byte control;
      private byte chrBank0;
      private byte chrBank1;
      private byte prgBank;

      public Rom(ByteBuffer rom) {
        this.rom = rom.asReadOnlyBuffer();
        this.shift = (short) 0x0001_0000;
        this.control = 0b0000_1100;
        this.chrBank0 = 0b0000_0000;
        this.chrBank1 = 0b0000_0000;
        this.prgBank = 0b0000_0000;
      }

      @Override
      public byte fetch(short address) {
        int mode = (control >>> 2) & 0b11;
        return switch (mode) {
          case 0 -> rom.get(Short.toUnsignedInt(address));
          case 1 -> throw new UnsupportedOperationException("mode = " + mode);
          case 2 -> throw new UnsupportedOperationException("mode = " + mode);
          case 3 -> {
            if (address >= 0x4000) {
              address = (short) (Short.toUnsignedInt(address) - 0x4000 + 0xc000);
            }
            yield rom.get(Short.toUnsignedInt(address));
          }
          default -> throw new IllegalArgumentException("bad mode: " + mode);
        };
      }

      @Override
      public void store(short address, byte data) {
        if ((data & 0b1000_0000) == 0b1000_0000) {
          this.shift = (short) 0x0001_0000;
          this.control = 0b0000_1100;
        }
      }
    }
  }
}
