package com.zacharyhirsch.moldynes.emulator.ppu;

final class BitPlane<T> {

  public T lo;
  public T hi;

  BitPlane(T lo, T hi) {
    this.lo = lo;
    this.hi = hi;
  }
}
