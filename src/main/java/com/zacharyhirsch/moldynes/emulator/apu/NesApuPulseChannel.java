package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuPulseChannel {
  
  private final short baseAddress;
  private final NesApuLengthCounter lengthCounter;

  private boolean enabled;
  private byte d;
  private byte c;
  private byte v;
  private byte e;
  private byte p;
  private byte n;
  private byte s;
  private int timer;
  private int timerCounter;

  NesApuPulseChannel(short baseAddress) {
    this.baseAddress = baseAddress;
    this.lengthCounter = new NesApuLengthCounter();
  }

  void enable(boolean enabled) {
    this.enabled = enabled;
    if (!enabled) {
      this.lengthCounter.clear();
    }
  }

  NesApuLengthCounter lengthCounter() {
    return lengthCounter;
  }

  void tick() {
    assert 0 <= timerCounter && timerCounter <= timer;
    if (timerCounter == 0) {
      timerCounter = timer;
    } else {
      timerCounter--;
    }
  }

  void write(int address, byte data) {
    switch (address - baseAddress) {
      case 0 -> {
        this.d = (byte) ((data & 0b1100_0000) >>> 6);
        lengthCounter.setHalted((data & 0b0010_0000) != 0);
        this.c = (byte) ((data & 0b0001_0000) >>> 4);
        this.v = (byte) ((data & 0b0000_1111) >>> 0);
      }
      case 1 -> {
        this.e = (byte) ((data & 0b1000_0000) >>> 7);
        this.p = (byte) ((data & 0b0111_0000) >>> 4);
        this.n = (byte) ((data & 0b0000_1000) >>> 3);
        this.s = (byte) ((data & 0b0000_0111) >>> 0);
      }
      case 2 -> {
        if (address == baseAddress + 2) {
          this.timer = (timer & 0b0111_0000_0000) | ((Byte.toUnsignedInt(data) & 0b1111_1111) << 0);
        }
      }
      case 3 -> {
        if (enabled) {
          lengthCounter.setValue((byte) ((data & 0b1111_1000) >>> 3));
        }
        this.timer = (timer & 0b0000_1111_1111) | ((Byte.toUnsignedInt(data) & 0b0000_0111) << 8);
        this.timerCounter = timer;
      }
      default ->
          throw new UnsupportedOperationException(
              "APU pulse@%04x: %04x <- %02x".formatted(baseAddress, address, data));
    }
  }
}
