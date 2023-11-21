package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Tya extends Instruction {

  private final UInt8 opcode;

  public Tya(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    context.registers().a = context.registers().y;
    context.registers().p.n = context.registers().a.bit(7) == 1;
    context.registers().p.z = context.registers().a.isZero();

    return new Result(() -> new UInt8[] {opcode}, () -> "TYA");
  }
}