package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class And extends Instruction {

  private final ReadableAddress<UInt8> address;

  public And(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.and(regs.a, address.fetch());
    regs.a = result.output();
    regs.p.n = result.n();
    regs.p.z = result.z();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
