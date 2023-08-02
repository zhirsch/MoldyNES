package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Sax extends Instruction {

  private final UInt8 opcode;
  private final StoreInstructionHelper helper;

  public Sax(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new StoreInstructionHelper("SAX", opcode);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x83 -> helper.storeIndirectX(context, () -> sax(context));
      case 0x87 -> helper.storeZeropage(context, () -> sax(context));
      case 0x8f -> helper.storeAbsolute(context, () -> sax(context));
      case 0x97 -> helper.storeZeropageY(context, () -> sax(context));
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 sax(NesCpuCycleContext context) {
    return NesAlu.and(context.registers().a, context.registers().x).output();
  }
}
