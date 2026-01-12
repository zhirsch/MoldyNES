package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidWriteError extends RuntimeException {

  public InvalidWriteError(int address) {
    super(String.format("unable to write address %04x", (short) address));
  }
}
