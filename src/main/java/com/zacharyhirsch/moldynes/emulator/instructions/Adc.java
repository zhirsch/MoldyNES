package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Adc extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Adc(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("ADC", opcode, this::adc);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x61 -> helper.fetchIndirectX(context);
      case 0x65 -> helper.fetchZeropage(context);
      case 0x69 -> helper.fetchImmediate(context);
      case 0x6d -> helper.fetchAbsolute(context);
      case 0x71 -> helper.fetchIndirectY(context);
      case 0x75 -> helper.fetchZeropageX(context);
      case 0x79 -> helper.fetchAbsoluteY(context);
      case 0x7d -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void adc(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.add(context.registers().a, data, context.registers().p.c);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    context.registers().p.c = result.c();
    context.registers().p.v = result.v();
  }
}
