package com.zacharyhirsch.moldynes.emulator.memory;

public interface WritableAddress<T> {
  void store(T value);
}
