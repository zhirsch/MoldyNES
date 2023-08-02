package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Hlt extends Instruction {

  private final UInt8 opcode;

  public Hlt(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return new Result(true, () -> new UInt8[] {opcode}, () -> "HLT");
  }
}
