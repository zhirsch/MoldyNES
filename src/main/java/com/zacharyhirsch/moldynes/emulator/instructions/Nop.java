package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Nop extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Nop(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("NOP", opcode, this::nop);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return switch (Byte.toUnsignedInt(opcode.value())) {
      case 0xea -> helper.executeImplied(memory, stack, regs);
      case 0x04, 0x1a, 0x3a, 0x44, 0x5a, 0x64, 0x7a, 0xda, 0xfa -> helper.executeZeropage(memory, stack, regs);
      case 0x0c -> helper.executeAbsolute(memory, stack, regs);
      case 0x14, 0x34, 0x54, 0x74, 0xd4, 0xf4 -> helper.executeZeropageX(memory, stack, regs);
      case 0x1c, 0x3c, 0x5c, 0x7c, 0xdc, 0xfc -> helper.executeAbsoluteX(memory, stack, regs);
      case 0x80, 0x89 -> helper.executeImmediate(memory, stack, regs);
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  private void nop(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 ignored) {}
}
