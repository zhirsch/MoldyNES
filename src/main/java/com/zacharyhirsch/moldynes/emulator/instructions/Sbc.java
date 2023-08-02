package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Sbc extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Sbc(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("SBC", opcode, this::sbc);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xe1 -> helper.fetchIndirectX(context);
      case 0xe5 -> helper.fetchZeropage(context);
      case 0xe9, 0xeb -> helper.fetchImmediate(context);
      case 0xed -> helper.fetchAbsolute(context);
      case 0xf1 -> helper.fetchIndirectY(context);
      case 0xf5 -> helper.fetchZeropageX(context);
      case 0xf9 -> helper.fetchAbsoluteY(context);
      case 0xfd -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void sbc(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.sub(context.registers().a, data, context.registers().p.c);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    context.registers().p.c = result.c();
    context.registers().p.v = result.v();
  }
}
