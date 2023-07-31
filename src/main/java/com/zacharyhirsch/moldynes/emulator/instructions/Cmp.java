package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public class Cmp extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Cmp(ReadableAddress<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.sub(regs.a, address.fetch());
    regs.p.n = result.n();
    regs.p.z = result.z();
    regs.p.c = result.c();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
