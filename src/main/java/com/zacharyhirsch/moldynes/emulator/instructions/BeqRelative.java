package com.zacharyhirsch.moldynes.emulator.instructions;

public class BeqRelative implements Instruction {

  private final byte relative;

  public BeqRelative(byte relative) {
    this.relative = relative;
  }

  @Override
  public String describe() {
    return String.format("BEQ #$%02x", relative);
  }
}
