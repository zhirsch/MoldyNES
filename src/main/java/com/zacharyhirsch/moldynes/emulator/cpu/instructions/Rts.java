package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Rts extends Instruction {

  private final UInt8 opcode;

  public Rts(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored1 = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 ignored2 = context.fetch(context.registers().sp.address());

    // Cycle 4
    UInt8 pcl = context.fetch(context.registers().sp.incrementAndGetAddress());

    // Cycle 5
    UInt8 pch = context.fetch(context.registers().sp.incrementAndGetAddress());

    // Cycle 6
    context.registers().pc.set(new UInt16(pch, pcl));
    UInt8 ignored3 = context.fetch(context.registers().pc.getAddressAndIncrement());

    return new Result(() -> new UInt8[] {opcode}, () -> "RTS");
  }
}
