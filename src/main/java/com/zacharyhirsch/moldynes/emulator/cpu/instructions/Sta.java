package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreInstruction;

public final class Sta {

  private Sta() {}

  public static final StoreInstruction VALUE = cpu -> cpu.state.a;
}
