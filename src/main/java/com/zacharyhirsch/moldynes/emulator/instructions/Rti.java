package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Rti extends Instruction {

  private final UInt8 opcode;

  public Rti(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored1 = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 ignored2 = context.fetch(context.registers().sp.address());

    // Cycle 4
    UInt8 p = context.fetch(context.registers().sp.incrementAndGetAddress());
    context.registers().p.fromByte(p);

    // Cycle 5
    UInt8 pcl = context.fetch(context.registers().sp.incrementAndGetAddress());

    // Cycle 6
    UInt8 pch = context.fetch(context.registers().sp.incrementAndGetAddress());

    // Partial cycle...
    context.registers().pc.set(new UInt16(pch, pcl));

    return new Result(() -> new UInt8[] {opcode}, () -> "RTI");
  }
}
