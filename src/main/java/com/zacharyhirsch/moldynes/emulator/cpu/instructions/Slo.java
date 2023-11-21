package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Slo extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Slo(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("SLO", opcode, this::slo);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x03 -> helper.executeIndirectX(context);
      case 0x07 -> helper.executeZeropage(context);
      case 0x0f -> helper.executeAbsolute(context);
      case 0x13 -> helper.executeIndirectY(context);
      case 0x17 -> helper.executeZeropageX(context);
      case 0x1b -> helper.executeAbsoluteY(context);
      case 0x1f -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 slo(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result asl = NesAlu.asl(data);
    context.registers().p.c = asl.c();

    NesAlu.Result or = NesAlu.or(context.registers().a, asl.output());
    context.registers().a = or.output();
    context.registers().p.n = or.n();
    context.registers().p.z = or.z();

    return asl.output();
  }
}
