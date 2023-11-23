package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreInstruction;

public final class Stx {

  private Stx() {}

  public static final StoreInstruction VALUE = cpu -> cpu.state.x;
}
