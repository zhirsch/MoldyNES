package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Sei extends PToggleInstruction {

  public Sei(UInt8 opcode) {
    super(opcode, p -> p.i = true);
  }
}
