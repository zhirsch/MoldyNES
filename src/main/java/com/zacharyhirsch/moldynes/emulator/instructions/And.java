package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class And extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public And(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("AND", opcode, this::and);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x21 -> helper.fetchIndirectX(context);
      case 0x25 -> helper.fetchZeropage(context);
      case 0x29 -> helper.fetchImmediate(context);
      case 0x2d -> helper.fetchAbsolute(context);
      case 0x31 -> helper.fetchIndirectY(context);
      case 0x35 -> helper.fetchZeropageX(context);
      case 0x39 -> helper.fetchAbsoluteY(context);
      case 0x3d -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void and(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.and(context.registers().a, data);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
  }
}
