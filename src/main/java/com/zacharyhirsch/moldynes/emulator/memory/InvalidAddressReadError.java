package com.zacharyhirsch.moldynes.emulator.memory;

public final class InvalidAddressReadError extends RuntimeException {

  public InvalidAddressReadError(int address) {
    this((short) address);
  }

  public InvalidAddressReadError(short address) {
    super(String.format("unable to read address %04x", address));
  }

  public InvalidAddressReadError(int address, String name) {
    this((short) address, name);
  }

  public InvalidAddressReadError(short address, String name) {
    super(String.format("unable to read address %04x (%s)", address, name));
  }
}
