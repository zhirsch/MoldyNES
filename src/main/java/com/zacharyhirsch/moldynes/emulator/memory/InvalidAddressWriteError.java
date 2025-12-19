package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidAddressWriteError extends RuntimeException {

  public InvalidAddressWriteError(int address) {
    this((short) address);
  }

  public InvalidAddressWriteError(short address) {
    super(String.format("unable to write address %04x", address));
  }

  public InvalidAddressWriteError(int address, String name) {
    this((short) address, name);
  }

  public InvalidAddressWriteError(short address, String name) {
    super(String.format("unable to write address %04x (%s)", address, name));
  }
}
