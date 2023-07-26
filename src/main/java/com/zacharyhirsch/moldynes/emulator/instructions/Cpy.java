package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public class Cpy implements Instruction {

  private final ReadableAddress<UInt8> address;

  public Cpy(ReadableAddress<UInt8> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    NesAlu.Result result = NesAlu.sub(regs.y, address.fetch(), true);
    regs.sr.n = result.n();
    regs.sr.z = result.z();
    regs.sr.c = result.c();
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
