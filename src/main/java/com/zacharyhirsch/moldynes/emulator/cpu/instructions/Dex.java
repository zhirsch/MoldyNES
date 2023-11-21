package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Dex extends Instruction {

  private final UInt8 opcode;

  public Dex(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    // Cycle 3
    NesAlu.Result result = NesAlu.dec(context.registers().x);
    context.registers().x = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();

    return new Result(() -> new UInt8[] {opcode}, () -> "DEX");
  }
}
