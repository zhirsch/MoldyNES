package com.zacharyhirsch.moldynes.emulator.instructions;

public class JmpAbsolute implements Instruction {

  private final short absolute;

  public JmpAbsolute(short absolute) {
    this.absolute = absolute;
  }

  @Override
  public String describe() {
    return String.format("JMP #$%04x", absolute);
  }
}
