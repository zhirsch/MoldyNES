package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.*;

public class Brk extends Instruction {

  private static final UInt16 NMI_VECTOR_LSB_ADDRESS = UInt16.cast(0xfffe);
  private static final UInt16 NMI_VECTOR_MSB_ADDRESS = UInt16.cast(0xffff);

  private final UInt8 opcode;

  public Brk(UInt8 opcode) {
    this.opcode = opcode;
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.getAddressAndIncrement());

    // Cycle 3
    context.store(
        context.registers().sp.getAddressAndDecrement(), context.registers().pc.address().msb());

    // Cycle 4
    context.store(
        context.registers().sp.getAddressAndDecrement(), context.registers().pc.address().lsb());

    // Cycle 5
    context.store(context.registers().sp.getAddressAndDecrement(), context.registers().p.toByte());

    // Cycle 6
    UInt8 adl = context.fetch(NMI_VECTOR_LSB_ADDRESS);

    // Cycle 7
    UInt8 adh = context.fetch(NMI_VECTOR_MSB_ADDRESS);

    // Partial cycle...
    context.registers().pc.set(new UInt16(adh, adl));

    return new Result(
        () -> new UInt8[] {opcode, ignored}, () -> String.format("BRK #$%s", ignored));
  }
}
