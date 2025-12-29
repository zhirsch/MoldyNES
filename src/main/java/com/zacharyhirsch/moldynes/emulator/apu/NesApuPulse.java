package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

final class NesApuPulse {

  private final NesClock clock;
  private final short baseAddress;
  private final NesApuLengthCounter length;

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

  private long lengthCounterHaltDelay;
  private boolean pendingLengthCounterHalt;

  private long lengthCounterValueDelay;
  private byte pendingLengthCounterValue;

  NesApuPulse(NesClock clock, short baseAddress) {
    this.clock = clock;
    this.baseAddress = baseAddress;
    this.length = new NesApuLengthCounter();
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
        this.c = (byte) ((data & 0b0001_0000) >>> 4);
        this.v = (byte) ((data & 0b0000_1111) >>> 0);
        lengthCounterHaltDelay = clock.getCycle() + 1;
        pendingLengthCounterHalt = (data & 0b0010_0000) != 0;
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
          lengthCounterValueDelay = clock.getCycle() + 1;
          pendingLengthCounterValue = (byte) ((data & 0b1111_1000) >>> 3);
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
