package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Ldx extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Ldx(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("LDX", opcode, this::ldx);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xa2 -> helper.fetchImmediate(context);
      case 0xa6 -> helper.fetchZeropage(context);
      case 0xae -> helper.fetchAbsolute(context);
      case 0xb6 -> helper.fetchZeropageY(context);
      case 0xbe -> helper.fetchAbsoluteY(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void ldx(NesCpuCycleContext context, UInt8 data) {
    context.registers().x = data;
    context.registers().p.n = context.registers().x.bit(7) == 1;
    context.registers().p.z = context.registers().x.isZero();
  }
}
