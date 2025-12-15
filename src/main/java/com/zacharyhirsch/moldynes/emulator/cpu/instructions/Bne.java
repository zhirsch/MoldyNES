package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesCpuStatusRegister;
import java.util.function.Predicate;

public class Bne extends Bxx {

  public Bne() {
    super(Predicate.not(NesCpuStatusRegister::z));
  }
}
