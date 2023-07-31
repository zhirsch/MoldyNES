package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public final class Dcp extends Instruction {

  private final Address<UInt8> address;

  public Dcp(Address<UInt8> address) {
    this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    UInt8 output = NesAlu.dec(address.fetch()).output();
    address.store(output);

    NesAlu.Result result = NesAlu.sub(regs.a, output);
    regs.p.n = result.n();
    regs.p.z = result.z();
    regs.p.c = result.c();
  }

  @Override
  public Argument getArgument() {
    return address;
  }
}
