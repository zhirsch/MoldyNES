package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Sta extends Instruction {

  private final UInt8 opcode;
  private final StoreInstructionHelper helper;

  public Sta(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new StoreInstructionHelper("STA", opcode);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x81 -> helper.storeIndirectX(context, () -> context.registers().a);
      case 0x85 -> helper.storeZeropage(context, () -> context.registers().a);
      case 0x8d -> helper.storeAbsolute(context, () -> context.registers().a);
      case 0x91 -> helper.storeIndirectY(context, () -> context.registers().a);
      case 0x95 -> helper.storeZeropageX(context, () -> context.registers().a);
      case 0x99 -> helper.storeAbsoluteY(context, () -> context.registers().a);
      case 0x9d -> helper.storeAbsoluteX(context, () -> context.registers().a);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }
}
