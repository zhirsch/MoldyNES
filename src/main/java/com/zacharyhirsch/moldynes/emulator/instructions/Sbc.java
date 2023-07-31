package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Sbc extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Sbc(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.sub(regs.a, address.fetch(), regs.p.c);
    regs.a = result.output();
    regs.p.n = result.n();
    regs.p.z = result.z();
    regs.p.c = result.c();
    regs.p.v = result.v();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
