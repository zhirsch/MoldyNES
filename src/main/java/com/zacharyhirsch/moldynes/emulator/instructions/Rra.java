package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Rra extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Rra(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("RRA", opcode, this::rra);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x63 -> helper.executeIndirectX(context);
      case 0x67 -> helper.executeZeropage(context);
      case 0x6f -> helper.executeAbsolute(context);
      case 0x73 -> helper.executeIndirectY(context);
      case 0x77 -> helper.executeZeropageX(context);
      case 0x7b -> helper.executeAbsoluteY(context);
      case 0x7f -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 rra(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result ror = NesAlu.ror(data, context.registers().p.c);

    NesAlu.Result add = NesAlu.add(context.registers().a, ror.output(), ror.c());
    context.registers().a = add.output();
    context.registers().p.n = add.n();
    context.registers().p.z = add.z();
    context.registers().p.c = add.c();
    context.registers().p.v = add.v();

    return ror.output();
  }
}
