package com.zacharyhirsch.moldynes.emulator.memory;

public class Immediate<T extends Number> implements ReadableAddress<T> {

  private final T immediate;

  public Immediate(T immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    if (immediate.getClass().equals(Byte.class)) {
      return String.format("#$%02X", immediate.byteValue());
    }
    return String.format("#$%04X", immediate.shortValue());
  }

  @Override
  public T fetch() {
    return immediate;
  }

  @Override
  public int getSize() {
    return immediate.getClass().equals(Byte.class) ? 1 : 2;
  }
}
