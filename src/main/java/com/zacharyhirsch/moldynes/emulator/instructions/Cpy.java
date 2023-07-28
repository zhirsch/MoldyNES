package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public class Cpy extends Instruction {

  private final ReadableAddress<UInt8> address;

  public Cpy(ReadableAddress<UInt8> address) {
        this.address = address;
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.sub(regs.y, address.fetch());
    regs.sr.n = result.n();
    regs.sr.z = result.z();
    regs.sr.c = result.c();
  }
  @Override
  public Argument getArgument() {
    return address;
  }

}
