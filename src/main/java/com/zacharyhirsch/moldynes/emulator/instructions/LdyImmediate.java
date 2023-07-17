package com.zacharyhirsch.moldynes.emulator.instructions;

public class LdyImmediate implements Instruction {
  private final byte immediate;

  public LdyImmediate(byte immediate) {
    this.immediate = immediate;
  }

  @Override
  public String describe() {
    return String.format("LDY #$%02x", immediate);
  }
}
