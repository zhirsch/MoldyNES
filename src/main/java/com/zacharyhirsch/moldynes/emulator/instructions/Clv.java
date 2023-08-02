package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Clv extends PToggleInstruction {

  public Clv(UInt8 opcode) {
    super(opcode, p -> p.v = false);
  }
}
