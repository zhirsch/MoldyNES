package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.StoreFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Sta {

  private Sta() {}

  public static final class OnStore implements StoreFunction {

    @Override
    public byte value(NesCpuState state) {
      return state.a;
    }
  }
}
