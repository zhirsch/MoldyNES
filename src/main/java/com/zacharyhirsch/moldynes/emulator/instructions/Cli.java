package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Cli extends PToggleInstruction {

  public Cli(UInt8 opcode) {
    super(opcode, p -> p.i = false);
  }
}
