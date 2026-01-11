package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidReadError extends RuntimeException {

  public InvalidReadError(int address) {
    super(String.format("unable to read address %04x", address));
  }

  public static byte throw_(int address) {
    throw new InvalidReadError(address);
  }
}
