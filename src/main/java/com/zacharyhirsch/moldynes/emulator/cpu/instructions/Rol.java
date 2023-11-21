package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Rol extends Instruction {

  private final UInt8 opcode;
  private final ReadModifyWriteInstructionHelper helper;

  public Rol(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new ReadModifyWriteInstructionHelper("ROL", opcode, this::rol);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0x26 -> helper.executeZeropage(context);
      case 0x2a -> helper.executeAccumulator(context);
      case 0x2e -> helper.executeAbsolute(context);
      case 0x36 -> helper.executeZeropageX(context);
      case 0x3e -> helper.executeAbsoluteX(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private UInt8 rol(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.rol(data, context.registers().p.c);
    context.registers().p.c = result.c();
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    return result.output();
  }
}
