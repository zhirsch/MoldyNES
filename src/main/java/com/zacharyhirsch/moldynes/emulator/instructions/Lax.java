package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Lax extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Lax(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("LAX", opcode, this::lax);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xa3 -> helper.fetchIndirectX(context);
      case 0xa7 -> helper.fetchZeropage(context);
      case 0xaf -> helper.fetchAbsolute(context);
      case 0xb3 -> helper.fetchIndirectY(context);
      case 0xb7 -> helper.fetchZeropageY(context);
      case 0xbf -> helper.fetchAbsoluteY(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void lax(NesCpuCycleContext context, UInt8 data) {
    context.registers().a = data;
    context.registers().x = data;
    context.registers().p.n = data.bit(7) == 1;
    context.registers().p.z = data.isZero();
  }
}
