package com.zacharyhirsch.moldynes.emulator.apu;

import com.zacharyhirsch.moldynes.emulator.NesClock;

final class NesApuDelayedValue<T> {

  private final NesClock clock;

  private T value;
  private T pendingValue;
  private long delay;

  NesApuDelayedValue(NesClock clock, T value) {
    this.clock = clock;
    this.value = value;
    this.pendingValue = value;
    this.delay = 0;
  }

  T getValue() {
    return value;
  }

  void setValue(T value, long delay) {
    this.pendingValue = value;
    this.delay = clock.getCycle() + delay;
  }

  boolean tick() {
    if (clock.getCycle() == delay) {
      value = pendingValue;
      return true;
    }
    return false;
  }
}
