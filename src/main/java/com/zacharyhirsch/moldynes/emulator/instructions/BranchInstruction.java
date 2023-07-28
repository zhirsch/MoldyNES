package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.StatusRegister;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.memory.ImmediateByte;
import java.util.function.Predicate;

public abstract class BranchInstruction extends Instruction {

  private final Registers regs;
  private final ImmediateByte immediate;
  private final Predicate<StatusRegister> predicate;

  public BranchInstruction(
      Registers regs, ImmediateByte immediate, Predicate<StatusRegister> predicate) {
    this.regs = regs;
    this.immediate = immediate;
    this.predicate = predicate;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName().toUpperCase() + " $" + resolve();
  }

  @Override
  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    if (predicate.test(regs.sr)) {
      regs.pc.offset(immediate.fetch());
    }
  }

  @Override
  public Argument getArgument() {
    return immediate;
  }

  private UInt16 resolve() {
    return UInt16.cast(regs.pc.address().value() + immediate.fetch().value());
  }
}
