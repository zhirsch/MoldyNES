package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Cld extends PToggleInstruction {

  public Cld(UInt8 opcode) {
    super(opcode, p -> p.d = false);
  }
}
