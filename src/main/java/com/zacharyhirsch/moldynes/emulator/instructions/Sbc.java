package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public final class Sbc implements Instruction {

  private final ReadableAddress<Byte> address;

  public Sbc(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.sub(regs.a, address.fetch(), regs.sr.c);
    regs.a = result.output();
    regs.sr.n = result.n();
    regs.sr.z = result.z();
    regs.sr.c = result.c();
    regs.sr.v = result.v();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
