package com.zacharyhirsch.moldynes.emulator.apu;


final class NesApuDmc {

  NesApuDmc() {}

  void tick() {}

  void write(int address, byte data) {
    switch (address) {
      case 0x4010 -> {}
      case 0x4011 -> {}
      case 0x4012 -> {}
      case 0x4013 -> {}
      default ->
          throw new UnsupportedOperationException("APU %04x [dmc] <- %02x".formatted(address, data));
    }
  }
}
