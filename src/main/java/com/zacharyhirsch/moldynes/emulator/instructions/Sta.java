package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;
import com.zacharyhirsch.moldynes.emulator.memory.WritableAddress;

public final class Sta implements Instruction {

  private final WritableAddress<Byte> address;

  public Sta(WritableAddress<Byte> address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " " + address;
  }

  @Override
  public void execute(Ram ram, Registers regs, Stack stack) {
    address.store(regs.ac);
  }
}
