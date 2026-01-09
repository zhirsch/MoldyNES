package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import java.util.BitSet;

public final class NesApuNoise {

  private static final int[] PERIODS = {
    4, 8, 16, 32, 64, 96, 128, 160, 202, 254, 380, 508, 762, 1016, 2034, 4068,
  };

  private final NesApuLengthCounter length;
  private final NesApuDelayedValue<Boolean> lengthCounterHalt;
  private final NesApuDelayedValue<Byte> lengthCounterValue;
  private final NesApuEnvelope envelope;
  private final NesApuTimer timer;

  private boolean enabled;
  private boolean mode;
  private int shift;

  NesApuNoise(NesClock clock) {
    this.length = new NesApuLengthCounter("noise");
    this.lengthCounterHalt = new NesApuDelayedValue<>(clock, false);
    this.lengthCounterValue = new NesApuDelayedValue<>(clock, (byte) 0);
    this.envelope = new NesApuEnvelope();
    this.timer = new NesApuTimer();
    this.enabled = true;
    this.mode = false;
    this.shift = 1;
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

  NesApuEnvelope envelope() {
    return envelope;
  }

  int getCurrentVolume() {
    if ((shift & 0b0000_0001) != 0) {
      return 0;
    }
    if (length.value() == 0) {
      return 0;
    }
    return envelope.getVolume();
  }

  void tick() {
    if (lengthCounterHalt.tick()) {
      length.setHalted(lengthCounterHalt.getValue());
    }
    if (lengthCounterValue.tick()) {
      length.reset(lengthCounterValue.getValue());
    }
    if (timer.tick()) {
      int feedback = bit(shift, 0) ^ bit(shift, mode ? 6 : 1);
      shift >>>= 1;
      shift = (shift & 0b0011_1111_1111_1111) | (feedback << 14);
    }
  }

  public void writeControl(byte data) {
    lengthCounterHalt.setValue((data & 0b0010_0000) != 0, 1);
    envelope.setLoop((data & 0b0010_0000) != 0);
    envelope.setConstant((data & 0b0001_0000) != 0);
    envelope.setEnvelope(data & 0b0000_1111);
  }

  public void writeMode(byte data) {
    mode = (data & 0b1000_0000) != 0;
    timer.setPeriod(PERIODS[(data & 0b0000_1111)]);
  }

  public void writeLength(byte data) {
    if (enabled) {
      lengthCounterValue.setValue((byte) ((data & 0b1111_1000) >>> 3), 1);
    }
    envelope.start();
  }

  private static int bit(int value, int bit) {
    return (short) ((value & (1 << bit)) >>> bit);
  }
}
