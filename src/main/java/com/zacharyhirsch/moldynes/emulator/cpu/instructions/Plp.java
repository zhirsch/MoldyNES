package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Plp extends Instruction {

  private final UInt8 opcode;

  public Plp(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored1 = context.fetch(context.registers().pc.address());

    // Cycle 3
    UInt8 ignored2 = context.fetch(context.registers().sp.address());

    // Cycle 4
    context.registers().p.fromByte(context.fetch(context.registers().sp.incrementAndGetAddress()));

    return new Result(() -> new UInt8[] {opcode}, () -> "PLP");
  }
}
