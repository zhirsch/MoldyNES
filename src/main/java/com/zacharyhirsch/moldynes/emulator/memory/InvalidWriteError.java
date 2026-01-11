package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidWriteError extends RuntimeException {

  private InvalidWriteError(int address) {
    super(String.format("unable to write address %04x", (short) address));
  }

  public static void throw_(int address, byte data) {
    throw new InvalidWriteError(address);
  }
}
