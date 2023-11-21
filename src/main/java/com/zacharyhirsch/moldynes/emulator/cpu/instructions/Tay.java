package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Tay extends Instruction {

  private final UInt8 opcode;

  public Tay(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    context.registers().y = context.registers().a;
    context.registers().p.n = context.registers().y.bit(7) == 1;
    context.registers().p.z = context.registers().y.isZero();

    return new Result(() -> new UInt8[] {opcode}, () -> "TAY");
  }
}
