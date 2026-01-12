package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidReadError extends RuntimeException {

  public InvalidReadError(int address) {
    super(String.format("unable to read address %04x", address));
  }
}
