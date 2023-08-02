package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Pha extends Instruction {

  private final UInt8 opcode;

  public Pha(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    context.store(context.registers().sp.getAddressAndDecrement(), context.registers().a);

    return new Result(() -> new UInt8[] {opcode}, () -> "PHA");
  }
}
