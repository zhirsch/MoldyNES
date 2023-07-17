package com.zacharyhirsch.moldynes.emulator.instructions;

public class LdaImmediate implements Instruction {

  private final byte immediate;

  public LdaImmediate(byte immediate) {
    this.immediate = immediate;
  }

  @Override
  public String describe() {
    return String.format("LDA #$%02x", immediate);
  }

}
