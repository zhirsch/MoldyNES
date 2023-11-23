package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Dcp extends Instruction {

//  private final UInt8 opcode;
//  private final ReadModifyWriteInstructionHelper helper;
//
//  public Dcp(UInt8 opcode) {
//    this.opcode = opcode;
//    this.helper = new ReadModifyWriteInstructionHelper("DCP", opcode, this::dcp);
//  }
//
//  @Override
//  public Result execute(NesCpuCycleContext context) {
//    return switch (Byte.toUnsignedInt(opcode.value())) {
//      case 0xc3 -> helper.executeIndirectX(context);
//      case 0xc7 -> helper.executeZeropage(context);
//      case 0xcf -> helper.executeAbsolute(context);
//      case 0xd3 -> helper.executeIndirectY(context);
//      case 0xd7 -> helper.executeZeropageX(context);
//      case 0xdb -> helper.executeAbsoluteY(context);
//      case 0xdf -> helper.executeAbsoluteX(context);
//      default -> throw new UnknownOpcodeException(opcode);
//    };
//  }
//
//  private UInt8 dcp(NesCpuCycleContext context, UInt8 data) {
//    UInt8 output = NesAlu.dec(data).output();
//
//    NesAlu.Result cmp = NesAlu.sub(context.registers().a, output);
//    context.registers().p.n = cmp.n();
//    context.registers().p.z = cmp.z();
//    context.registers().p.c = cmp.c();
//
//    return output;
//  }
}
