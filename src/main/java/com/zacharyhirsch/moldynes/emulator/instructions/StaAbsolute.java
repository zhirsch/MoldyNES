package com.zacharyhirsch.moldynes.emulator.instructions;

public class StaAbsolute implements Instruction {

  private final short absolute;

  public StaAbsolute(short absolute) {
    this.absolute = absolute;
  }

  @Override
  public String describe() {
    return String.format("STA #$%04x", absolute);
  }

}
