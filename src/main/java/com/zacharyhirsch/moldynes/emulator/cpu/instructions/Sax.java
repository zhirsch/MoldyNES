package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreInstruction;

public final class Sax {

  private Sax() {}

  public static final StoreInstruction VALUE = cpu -> (byte) (cpu.state.a & cpu.state.x);
}
