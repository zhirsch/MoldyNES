package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Sec extends PToggleInstruction {

  public Sec(UInt8 opcode) {
    super(opcode, p -> p.c = true);
  }
}
