package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

public final class NesApuPulse {

  private static final int[][] SEQUENCES = {
    {0, 0, 0, 0, 0, 0, 0, 1},
    {0, 0, 0, 0, 0, 0, 1, 1},
    {0, 0, 0, 0, 1, 1, 1, 1},
    {1, 1, 1, 1, 1, 1, 0, 0},
  };

  private final NesClock clock;
  private final int index;
  private final NesApuLengthCounter length;
  private final NesApuTimer timer;
  private final NesApuDelayedValue<Boolean> lengthCounterHalt;
  private final NesApuDelayedValue<Byte> lengthCounterValue;
  private final NesApuEnvelope envelope;
  private final NesApuSweep sweep;

  private boolean enabled;

  private int duty;
  private int sequenceIndex;

  NesApuPulse(NesClock clock, int index) {
    this.clock = clock;
    this.index = index;
    this.length = new NesApuLengthCounter(getName());
    this.timer = new NesApuTimer();
    this.lengthCounterHalt = new NesApuDelayedValue<>(clock, false);
    this.lengthCounterValue = new NesApuDelayedValue<>(clock, (byte) 0);
    this.envelope = new NesApuEnvelope();
    this.sweep = new NesApuSweep(this);
    this.enabled = true;
    this.duty = 0;
    this.sequenceIndex = 0;
  }

  int getIndex() {
    return index;
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

  NesApuTimer timer() {
    return timer;
  }

  NesApuEnvelope envelope() {
    return envelope;
  }

  NesApuSweep sweep() {
    return sweep;
  }

  int getCurrentVolume() {
    if (!enabled) {
      return 0;
    }
    if (SEQUENCES[duty][sequenceIndex] == 0) {
      return 0;
    }
    if (length.value() == 0) {
      return 0;
    }
    if (sweep.isMuting()) {
      return 0;
    }
    if (timer.getPeriod() < 8) {
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
    if (clock.getCycle() % 2 == 0) {
      if (timer.tick()) {
        sequenceIndex--;
        if (sequenceIndex < 0) {
          sequenceIndex = 7;
        }
      }
    }
  }

  public void writeControl(byte data) {
    duty = (data & 0b1100_0000) >>> 6;
    lengthCounterHalt.setValue((data & 0b0010_0000) != 0, 1);
    envelope.setLoop((data & 0b0010_0000) != 0);
    envelope.setConstant((data & 0b0001_0000) != 0);
    envelope.setEnvelope(data & 0b0000_1111);
  }

  public void writeSweep(byte data) {
    sweep.setEnabled((data & 0b1000_0000) != 0);
    sweep.setPeriod(((data & 0b0111_0000) >>> 4) + 1);
    sweep.setNegate((data & 0b0000_1000) != 0);
    sweep.setShift(data & 0b0000_0111);
  }

  public void writeTimerLo(byte data) {
    timer.setPeriodLo(Byte.toUnsignedInt(data));
  }

  public void writeTimerHi(byte data) {
    if (enabled) {
      lengthCounterValue.setValue((byte) ((data & 0b1111_1000) >>> 3), 1);
    }
    timer.setPeriodHi(data & 0b0000_0111);
    sequenceIndex = 0;
    envelope.start();
  }

  private String getName() {
    return "pulse%d".formatted(index);
  }
}
