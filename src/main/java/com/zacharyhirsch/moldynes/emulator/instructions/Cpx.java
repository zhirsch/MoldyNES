package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public final class Cpx extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Cpx(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("CPX", opcode, this::cpx);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xe0 -> helper.fetchImmediate(context);
      case 0xe4 -> helper.fetchZeropage(context);
      case 0xec -> helper.fetchAbsolute(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void cpx(NesCpuCycleContext context, UInt8 data) {
    NesAlu.Result result = NesAlu.sub(context.registers().x, data);
    context.registers().p.n = result.n();
    context.registers().p.z = result.z();
    context.registers().p.c = result.c();
  }
}
