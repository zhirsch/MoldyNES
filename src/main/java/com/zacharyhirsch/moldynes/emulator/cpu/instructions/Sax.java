package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.StoreFunction;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuState;

public final class Sax {

  private Sax() {}

  public static final class OnStore implements StoreFunction {

    @Override
    public byte value(NesCpuState state) {
      return (byte) (state.a & state.x);
    }
  }
}
