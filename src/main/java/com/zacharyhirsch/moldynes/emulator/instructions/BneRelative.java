package com.zacharyhirsch.moldynes.emulator.instructions;

public class BneRelative implements Instruction {

  private final byte relative;

  public BneRelative(byte relative) {
    this.relative = relative;
  }

  @Override
  public String describe() {
    return String.format("BNE #$%02x", relative);
  }
}
