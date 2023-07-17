package com.zacharyhirsch.moldynes.emulator.instructions;

public class LdxImmediate implements Instruction {

  private final byte immediate;

  public LdxImmediate(byte immediate) {
    this.immediate = immediate;
  }

  @Override
  public String describe() {
    return String.format("LDX #$%02x", immediate);
  }
}
