package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Isb extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Isb(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("ISB", opcode, this::isb);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xe3 -> helper.executeIndirectX(context);
      case 0xe7 -> helper.executeZeropage(context);
      case 0xef -> helper.executeAbsolute(context);
      case 0xf3 -> helper.executeIndirectY(context);
      case 0xf7 -> helper.executeZeropageX(context);
      case 0xfb -> helper.executeAbsoluteY(context);
      case 0xff -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 isb(NesCpuCycleContext context, UInt8 data) {
    UInt8 output = NesAlu.inc(data).output();

    NesAlu.Result result = NesAlu.sub(context.registers().a, output, context.registers().p.c);
    context.registers().a = result.output();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    context.registers().p.c = result.c();
    context.registers().p.v = result.v();

    return output;
  }
}
