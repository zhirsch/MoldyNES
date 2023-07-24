package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.memory.Address;

public class Dec implements Instruction {

  private final Address<Byte> address;

  public Dec(Address<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address.toString();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    byte output = (byte) (address.fetch() - 1);
    address.store(output);

    regs.sr.n = output < 0;
    regs.sr.z = output == 0;
  }

  @Override
  public int getSize() {
    return 1 + address.getSize();
  }
}
