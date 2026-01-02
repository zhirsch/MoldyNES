package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

public final class NesApuTriangle {

  private final NesClock clock;
  private final NesApuLengthCounter length;

  private boolean enabled;

  private long lengthCounterHaltDelay;
  private boolean pendingLengthCounterHalt;

  private long lengthCounterValueDelay;
  private byte pendingLengthCounterValue;

  NesApuTriangle(NesClock clock) {
    this.clock = clock;
    this.length = new NesApuLengthCounter("triangle");
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

  public void writeControl(byte data) {
    lengthCounterHaltDelay = clock.getCycle() + 1;
    pendingLengthCounterHalt = (data & 0b1000_0000) != 0;
  }

  public void writeTimerLo(byte data) {}

  public void writeTimerHi(byte data) {
    if (enabled) {
      lengthCounterValueDelay = clock.getCycle() + 1;
      pendingLengthCounterValue = (byte) ((data & 0b1111_1000) >>> 3);
    }
  }
}
