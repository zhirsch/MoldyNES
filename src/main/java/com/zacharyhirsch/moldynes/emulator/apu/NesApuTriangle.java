package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

public final class NesApuTriangle {

  private static final int[] SEQUENCE = {
    15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0,
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
  };

  private final NesApuLengthCounter length;
  private final NesApuLinearCounter linear;
  private final NesApuTimer timer;
  private final NesApuDelayedValue<Boolean> lengthCounterHalt;
  private final NesApuDelayedValue<Byte> lengthCounterValue;

  private boolean enabled;
  private int sequenceIndex;

  NesApuTriangle(NesClock clock) {
    this.length = new NesApuLengthCounter("triangle");
    this.linear = new NesApuLinearCounter();
    this.timer = new NesApuTimer();
    this.lengthCounterHalt = new NesApuDelayedValue<>(clock, false);
    this.lengthCounterValue = new NesApuDelayedValue<>(clock, (byte) 0);
    this.enabled = true;
    this.sequenceIndex = 0;
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

  NesApuLinearCounter linear() {
    return linear;
  }

  int getCurrentVolume() {
    return SEQUENCE[sequenceIndex];
  }

  void tick() {
    if (lengthCounterHalt.tick()) {
      length.setHalted(lengthCounterHalt.getValue());
    }
    if (lengthCounterValue.tick()) {
      length.reset(lengthCounterValue.getValue());
    }
    if (timer.tick()) {
      if (length.value() > 0 && linear.value() > 0) {
        sequenceIndex = (sequenceIndex + 1) % 32;
      }
    }
  }

  public void writeControl(byte data) {
    lengthCounterHalt.setValue((data & 0b1000_0000) != 0, 1);
    linear.setControl((data & 0b1000_0000) != 0);
    linear.setPeriod(data & 0b0111_1111);
  }

  public void writeTimerLo(byte data) {
    timer.setPeriodLo(Byte.toUnsignedInt(data));
  }

  public void writeTimerHi(byte data) {
    if (enabled) {
      lengthCounterValue.setValue((byte) ((data & 0b1111_1000) >>> 3), 1);
    }
    timer.setPeriodHi(data & 0b0000_0111, 1);
    linear.setReload(true);
  }
}
