package com.zacharyhirsch.moldynes.emulator.memory;

public interface ReadableAddress<T> {
  T fetch();
}
