package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Ldx {

  private Ldx() {}

  public static final class OnFetch implements ModifyFunction {

    @Override
    public void modify(NesCpuState state) {
      state.x = state.data;
      state.pN(state.data < 0);
      state.pZ(state.data == 0);
    }
  }
}