package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;

public class Sec extends Instruction {

  private final UInt8 opcode;
  private final InstructionHelper helper;

  public Sec(UInt8 opcode) {
    this.opcode = opcode;
    this.helper = new InstructionHelper("SEC", opcode, this::sec);
  }

  @Override
  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    return helper.executeImplied(memory, stack, regs);
  }

  private void sec(NesCpuMemory memory, NesCpuStack stack, Registers regs, UInt8 ignored) {
    regs.p.c = true;
  }
}
