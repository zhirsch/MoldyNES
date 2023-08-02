package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Clc extends PToggleInstruction {

  public Clc(UInt8 opcode) {
    super(opcode, p -> p.c = false);
  }
}
