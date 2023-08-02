package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Eor extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Eor(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("EOR", opcode, this::eor);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x41 -> helper.fetchIndirectX(context);
      case 0x45 -> helper.fetchZeropage(context);
      case 0x49 -> helper.fetchImmediate(context);
      case 0x4d -> helper.fetchAbsolute(context);
      case 0x51 -> helper.fetchIndirectY(context);
      case 0x55 -> helper.fetchZeropageX(context);
      case 0x59 -> helper.fetchAbsoluteY(context);
      case 0x5d -> helper.fetchAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void eor(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.xor(context.registers().a, data);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
  }
}
