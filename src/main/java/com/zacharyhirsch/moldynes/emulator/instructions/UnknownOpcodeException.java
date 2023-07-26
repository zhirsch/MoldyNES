package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class UnknownOpcodeException extends RuntimeException {

  public UnknownOpcodeException(UInt8 opcode) {
    super(String.format("unknown opcode %s", opcode));
  }
}
