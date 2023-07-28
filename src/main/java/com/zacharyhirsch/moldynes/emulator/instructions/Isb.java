package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Isb extends Instruction {

  private final Address<UInt8> address;

  public Isb(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 output = NesAlu.inc(address.fetch()).output();
    address.store(output);

    NesAlu.Result result = NesAlu.sub(regs.a, output, regs.sr.c);
    regs.a = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
    regs.sr.c = result.c();
    regs.sr.v = result.v();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
