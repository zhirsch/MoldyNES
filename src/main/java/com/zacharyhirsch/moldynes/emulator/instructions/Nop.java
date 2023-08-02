package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Nop extends Instruction {

  private final UInt8 opcode;
  private final FetchInstructionHelper helper;

  public Nop(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new FetchInstructionHelper("NOP", opcode, this::nop);
  }

  @Override
  public Result execute(NesCpuCycleContext context) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xea, 0x1a, 0x3a, 0x5a, 0x7a, 0xda, 0xfa -> executeImplied(context);
      case 0x04, 0x44, 0x64 -> helper.fetchZeropage(context);
      case 0x0c -> helper.fetchAbsolute(context);
      case 0x14, 0x34, 0x54, 0x74, 0xd4, 0xf4 -> helper.fetchZeropageX(context);
      case 0x1c, 0x3c, 0x5c, 0x7c, 0xdc, 0xfc -> helper.fetchAbsoluteX(context);
      case 0x80, 0x89 -> helper.fetchImmediate(context);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private Result executeImplied(NesCpuCycleContext context) {
    // Cycle 2
    UInt8 ignored = context.fetch(context.registers().pc.address());

    return new Result(() -> new UInt8[] {opcode}, () -> "NOP");
  }

  private void nop(NesCpuCycleContext context, UInt8 ignored) {}
}
