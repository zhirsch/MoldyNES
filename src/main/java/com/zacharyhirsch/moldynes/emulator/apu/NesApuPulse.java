package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;

final class NesApuPulse {

  private final NesClock clock;
  private final int index;
  private final NesApuLengthCounter length;

  private boolean enabled;

  private long lengthCounterHaltDelay;
  private boolean pendingLengthCounterHalt;

  private long lengthCounterValueDelay;
  private byte pendingLengthCounterValue;

  NesApuPulse(NesClock clock, int index) {
    this.clock = clock;
    this.index = index;
    this.length = new NesApuLengthCounter(getName());
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
      case 0x4000, 0x4004 -> {
        lengthCounterHaltDelay = clock.getCycle() + 1;
        pendingLengthCounterHalt = (data & 0b0010_0000) != 0;
      }
      case 0x4001, 0x4005 -> {}
      case 0x4002, 0x4006 -> {}
      case 0x4003, 0x4007 -> {
        if (enabled) {
          lengthCounterValueDelay = clock.getCycle() + 1;
          pendingLengthCounterValue = (byte) ((data & 0b1111_1000) >>> 3);
        }
      }
      default -> throw new InvalidAddressWriteError(address);
    }
  }

  private String getName() {
    return "pulse%d".formatted(index);
  }
}
