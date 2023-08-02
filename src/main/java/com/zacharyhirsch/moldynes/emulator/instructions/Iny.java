package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Iny extends Instruction {

  private final UInt8 opcode;

  public Iny(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    NesAlu.Result result = NesAlu.inc(context.registers().y);
    context.registers().y = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();

    return new Result(() -> new UInt8[] {opcode}, () -> "INY");
  }
}
