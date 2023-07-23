package com.zacharyhirsch.moldynes.emulator.memory;

public class Immediate<T extends Number> implements ReadableAddress<T> {

  private final T immediate;

  public Immediate(T immediate) {
    this.immediate = immediate;
  }

  @Override
  public String toString() {
    return String.format("#$%04x", immediate);
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
