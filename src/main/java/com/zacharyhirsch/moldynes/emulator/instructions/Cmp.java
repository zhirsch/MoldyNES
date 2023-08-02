package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Cmp extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Cmp(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("CMP", opcode, this::cmp);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xc1 -> helper.fetchIndirectX(context);
      case 0xc5 -> helper.fetchZeropage(context);
      case 0xc9 -> helper.fetchImmediate(context);
      case 0xcd -> helper.fetchAbsolute(context);
      case 0xd1 -> helper.fetchIndirectY(context);
      case 0xd5 -> helper.fetchZeropageX(context);
      case 0xd9 -> helper.fetchAbsoluteY(context);
      case 0xdd -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void cmp(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.sub(context.registers().a, data);
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    context.registers().p.c = result.c();
  }
}
