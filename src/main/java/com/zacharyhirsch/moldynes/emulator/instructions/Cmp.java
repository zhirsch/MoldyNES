package com.zacharyhirsch.moldynes.emulator.instructions;

import static java.lang.Byte.toUnsignedInt;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

public class Cmp implements Instruction {

  private final ReadableAddress<Byte> address;

  public Cmp(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    int unsignedA = toUnsignedInt(regs.a);
    int unsignedInput = toUnsignedInt(address.fetch());
    int result = unsignedA - unsignedInput;

    if (unsignedA < unsignedInput) {
      regs.sr.n = (result & 0b1000_0000) == 0b1000_0000;
      regs.sr.z = false;
      regs.sr.c = false;
    } else if (unsignedA == unsignedInput) {
      regs.sr.n = false;
      regs.sr.z = true;
      regs.sr.c = true;
    } else if (unsignedA > unsignedInput) {
      regs.sr.n = (result & 0b1000_0000) == 0b1000_0000;
      regs.sr.z = false;
      regs.sr.c = true;
    }
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
