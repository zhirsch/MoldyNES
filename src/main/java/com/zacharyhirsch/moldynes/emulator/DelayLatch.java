package com.zacharyhirsch.moldynes.emulator;

public final class DelayLatch<T> {

  private T value;
  private T pending;

  public DelayLatch(T value) {
    this.value = value;
    this.pending = value;
  }

  public void tick() {
    value = pending;
  }

  public T value() {
    return this.value;
  }

  public void set(T value) {
    pending = value;
  }
}
