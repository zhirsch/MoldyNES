package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Tax extends Instruction {

  private final UInt8 opcode;

  public Tax(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    context.registers().x = context.registers().a;
    context.registers().p.n = context.registers().x.bit(7) == 1;
    context.registers().p.z = context.registers().x.isZero();

    return new Result(() -> new UInt8[] {opcode}, () -> "TAX");
  }
}
