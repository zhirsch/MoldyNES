package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Lda extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Lda(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("LDA", opcode, this::lda);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xa1 -> helper.fetchIndirectX(context);
      case 0xa5 -> helper.fetchZeropage(context);
      case 0xa9 -> helper.fetchImmediate(context);
      case 0xad -> helper.fetchAbsolute(context);
      case 0xb1 -> helper.fetchIndirectY(context);
      case 0xb5 -> helper.fetchZeropageX(context);
      case 0xb9 -> helper.fetchAbsoluteY(context);
      case 0xbd -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void lda(NesCpuCycleContext context, UInt8 data) {
    context.registers().a = data;
    context.registers().p.n = context.registers().a.bit(7) == 1;
    context.registers().p.z = context.registers().a.isZero();
  }
}
