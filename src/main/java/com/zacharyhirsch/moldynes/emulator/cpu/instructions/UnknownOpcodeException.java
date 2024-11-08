package com.zacharyhirsch.moldynes.emulator.cpu.instructions;


public final class UnknownOpcodeException extends RuntimeException {

  public UnknownOpcodeException(byte opcode) {
    super(String.format("unknown opcode %02x", opcode));
  }
}
