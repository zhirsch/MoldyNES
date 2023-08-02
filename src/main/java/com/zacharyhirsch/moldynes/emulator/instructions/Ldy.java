package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Ldy extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Ldy(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("LDY", opcode, this::ldy);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xa0 -> helper.fetchImmediate(context);
      case 0xa4 -> helper.fetchZeropage(context);
      case 0xac -> helper.fetchAbsolute(context);
      case 0xb4 -> helper.fetchZeropageX(context);
      case 0xbc -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void ldy(NesCpuCycleContext context, UInt8 data) {
    context.registers().y = data;
    context.registers().p.n = context.registers().y.bit(7) == 1;
    context.registers().p.z = context.registers().y.isZero();
  }
}
