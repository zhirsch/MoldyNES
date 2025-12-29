package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

final class NesApuNoise {

  private final NesClock clock;
  private final NesApuLengthCounter length;

  private boolean enabled;

  private long lengthCounterValueDelay;
  private byte pendingLengthCounterValue;

  NesApuNoise(NesClock clock) {
    this.clock = clock;
    this.length = new NesApuLengthCounter();
    this.enabled = true;
    this.lengthCounterValueDelay = 0;
    this.pendingLengthCounterValue = 0;
  }

  void enable(boolean enabled) {
    this.enabled = enabled;
    if (!enabled) {
      this.length.clear();
    }
  }

  NesApuLengthCounter length() {
    return length;
  }

  void tick() {
    if (clock.getCycle() == lengthCounterValueDelay) {
      length.reset(pendingLengthCounterValue);
    }
  }

  void write(int address, byte data) {
    switch (address) {
      case 0x400c -> {}
      case 0x400d -> {}
      case 0x400e -> {}
      case 0x400f -> {
        if (enabled) {
          lengthCounterValueDelay = clock.getCycle() + 1;
          pendingLengthCounterValue = (byte) ((data & 0b1111_1000) >>> 3);
        }
      }
      default ->
          throw new UnsupportedOperationException(
              "APU %04x [noise] <- %02x".formatted(address, data));
    }
  }
}
