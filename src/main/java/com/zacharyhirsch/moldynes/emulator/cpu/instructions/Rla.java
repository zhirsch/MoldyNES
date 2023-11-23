package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Rla extends Instruction {

//  private final UInt8 opcode;
//  private final ReadModifyWriteInstructionHelper helper;
//
//  public Rla(UInt8 opcode) {
//    this.opcode = opcode;
//    this.helper = new ReadModifyWriteInstructionHelper("RLA", opcode, this::rla);
//  }
//
//  @Override
//  public Result execute(NesCpuCycleContext context) {
//    return switch (Byte.toUnsignedInt(opcode.value())) {
//      case 0x23 -> helper.executeIndirectX(context);
//      case 0x27 -> helper.executeZeropage(context);
//      case 0x2f -> helper.executeAbsolute(context);
//      case 0x33 -> helper.executeIndirectY(context);
//      case 0x37 -> helper.executeZeropageX(context);
//      case 0x3b -> helper.executeAbsoluteY(context);
//      case 0x3f -> helper.executeAbsoluteX(context);
//      default -> throw new UnknownOpcodeException(opcode);
//    };
//  }
//
//  private UInt8 rla(NesCpuCycleContext context, UInt8 data) {
//    NesAlu.Result rol = NesAlu.rol(data, context.registers().p.c);
//    context.registers().p.c = rol.c();
//
//    NesAlu.Result and = NesAlu.and(context.registers().a, rol.output());
//    context.registers().a = and.output();
//    context.registers().p.n = and.n();
//    context.registers().p.z = and.z();
//
//    return rol.output();
//  }
}
