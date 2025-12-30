package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidAddressWriteError;

final class NesApuPulse {

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
    this.enabled = true;
    this.duty = 0;
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

  NesApuEnvelope envelope() {
    return envelope;
  }

  int getCurrentVolume() {
    if (!enabled) {
      return 0;
    }
    if (SEQUENCES[duty][sequenceIndex] == 0) {
      return 0;
    }
    // TODO: overflow from the sweep unit's adder is silencing the channel
    if (length.value() == 0) {
      return 0;
    }
    if (timer.getRate() < 8) {
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

  void write(int address, byte data) {
    switch (address) {
      case 0x4000, 0x4004 -> {
        duty = (data & 0b1100_0000) >>> 6;
        lengthCounterHalt.setValue((data & 0b0010_0000) != 0, 1);
        envelope.setLoop((data & 0b0010_0000) != 0);
        envelope.setConstant((data & 0b0001_0000) != 0);
        envelope.setEnvelope(data & 0b0000_1111);
      }
      case 0x4001, 0x4005 -> {
        if ((data & 0b1000_0000) != 0) {
          throw new UnsupportedOperationException("pulse%d sweep".formatted(index));
        }
      }
      case 0x4002, 0x4006 -> timer.setRateLo(Byte.toUnsignedInt(data));
      case 0x4003, 0x4007 -> {
        if (enabled) {
          lengthCounterValue.setValue((byte) ((data & 0b1111_1000) >>> 3), 1);
        }
        timer.setRateHi(data & 0b0000_0111);
        sequenceIndex = 0;
        envelope.start();
      }
      default -> throw new InvalidAddressWriteError(address);
    }
  }

  private String getName() {
    return "pulse%d".formatted(index);
  }
}
