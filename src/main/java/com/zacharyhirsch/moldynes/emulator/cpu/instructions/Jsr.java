package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Jsr extends Instruction {

  private final UInt8 opcode;

  public Jsr(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    UInt8 ignored = context.fetch(context.registers().sp.address());

    // Cycle 4
    context.store(
        context.registers().sp.getAddressAndDecrement(), context.registers().pc.address().msb());

    // Cycle 5
    context.store(
        context.registers().sp.getAddressAndDecrement(), context.registers().pc.address().lsb());

    // Cycle 6
    UInt8 adh = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 7
    context.registers().pc.set(new UInt16(adh, adl));

    return new Result(
        () -> new UInt8[] {opcode, adl, adh}, () -> String.format("JSR $%s", new UInt16(adh, adl)));
  }
}
