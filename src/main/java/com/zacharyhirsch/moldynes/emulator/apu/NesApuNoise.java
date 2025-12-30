package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;

final class NesApuNoise {

  private final NesClock clock;
  private final NesApuLengthCounter length;

  private boolean enabled;

  private long lengthCounterHaltDelay;
  private boolean pendingLengthCounterHalt;

  private long lengthCounterValueDelay;
  private byte pendingLengthCounterValue;

  NesApuNoise(NesClock clock) {
    this.clock = clock;
    this.length = new NesApuLengthCounter("noise");
    this.enabled = true;
    this.lengthCounterHaltDelay = 0;
    this.pendingLengthCounterHalt = false;
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

  int getCurrentVolume() {
    return 0;
  }

  void tick() {
    if (clock.getCycle() == lengthCounterHaltDelay) {
      length.setHalted(pendingLengthCounterHalt);
    }
    if (clock.getCycle() == lengthCounterValueDelay) {
      length.reset(pendingLengthCounterValue);
    }
  }

  void write(int address, byte data) {
    switch (address) {
      case 0x400c -> {
        lengthCounterHaltDelay = clock.getCycle() + 1;
        pendingLengthCounterHalt = (data & 0b0010_0000) != 0;
      }
      case 0x400d -> {}
      case 0x400e -> {}
      case 0x400f -> {
        if (enabled) {
          lengthCounterValueDelay = clock.getCycle() + 1;
          pendingLengthCounterValue = (byte) ((data & 0b1111_1000) >>> 3);
        }
      }
      default -> throw new InvalidAddressWriteError(address);
    }
  }
}
