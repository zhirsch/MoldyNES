package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.ReadableAddress;

import static java.lang.Byte.toUnsignedInt;

public class Cpx implements Instruction {

  private final ReadableAddress<Byte> address;

  public Cpx(ReadableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    int unsignedX = toUnsignedInt(regs.x);
    int unsignedInput = toUnsignedInt(address.fetch());
    int result = unsignedX - unsignedInput;

    if (unsignedX < unsignedInput) {
      regs.sr.n = (result & 0b1000_0000) == 0b1000_0000;
      regs.sr.z = false;
      regs.sr.c = false;
    } else if (unsignedX == unsignedInput) {
      regs.sr.n = false;
      regs.sr.z = true;
      regs.sr.c = true;
    } else if (unsignedX > unsignedInput) {
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
