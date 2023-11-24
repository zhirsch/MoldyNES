package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.addressing.StoreInstruction;

public class Sty {

  private Sty() {}

  public static final StoreInstruction VALUE = (cpu, adh, adl) -> cpu.state.y;
}
