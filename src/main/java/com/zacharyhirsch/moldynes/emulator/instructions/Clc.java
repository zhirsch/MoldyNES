package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Clc extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Clc(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("CLC", opcode, this::clc);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return helper.executeImplied(memory, stack, regs);
  }

  private void clc(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 ignored) {
    regs.p.c = false;
  }
}
