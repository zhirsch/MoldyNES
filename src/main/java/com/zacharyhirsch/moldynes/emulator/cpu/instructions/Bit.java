package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Bit {

  private Bit() {}

  public static final class OnFetch implements ModifyFunction {

    @Override
    public void modify(NesCpuState state) {
      state.pN((state.data & 0b1000_0000) == 0b1000_0000);
      state.pV((state.data & 0b0100_0000) == 0b0100_0000);
      state.pZ((state.data & state.a) == 0);
    }
  }
}
