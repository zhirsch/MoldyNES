package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Ora extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Ora(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("ORA", opcode, this::or);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x01 -> helper.fetchIndirectX(context);
      case 0x05 -> helper.fetchZeropage(context);
      case 0x09 -> helper.fetchImmediate(context);
      case 0x0d -> helper.fetchAbsolute(context);
      case 0x11 -> helper.fetchIndirectY(context);
      case 0x15 -> helper.fetchZeropageX(context);
      case 0x19 -> helper.fetchAbsoluteY(context);
      case 0x1d -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void or(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.or(context.registers().a, data);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
  }
}
