package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.FinishFunction;
import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluOr;

public final class Ora {

  private Ora() {}

  public static final class OnFetch implements ModifyFunction {

    @Override
    public void modify(NesCpuState state) {
      state.alu = new NesAluOr(state.a, state.data);
    }
  }

  public static final class OnFinish implements FinishFunction {

    @Override
    public void finish(NesCpuState state) {
      state.a = state.alu.output();
      state.pN(state.alu.n());
      state.pZ(state.alu.z());
    }
  }
}
