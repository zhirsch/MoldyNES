package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Sed extends PToggleInstruction {

  public Sed(UInt8 opcode) {
    super(opcode, p -> p.d = true);
  }
}
