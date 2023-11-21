package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.FinishFunction;
import com.zacharyhirsch.moldynes.emulator.ModifyFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuCycle;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuDecode;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;
import com.zacharyhirsch.moldynes.emulator.cpu.alu.NesAluSub;

public final class Cmp {

  private Cmp() {}

  public static final class OnFetch implements ModifyFunction {

    @Override
    public void modify(NesCpuState state) {
      state.alu = new NesAluSub(state.a, state.data, false);
    }
  }

  public static final class OnFinish implements FinishFunction {

    @Override
    public void finish(NesCpuState state) {
      state.pN(state.alu.n());
      state.pZ(state.alu.z());
      state.pC(state.alu.c());
    }
  }
}
