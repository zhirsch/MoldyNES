package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.HaltException;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import java.util.function.Supplier;

public abstract class Instruction {

  public void execute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {}

  public Result execute2(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    try {
      execute(memory, stack, regs);
      return new Result(
          1,
          false,
          () -> {
            throw new RuntimeException();
          },
          this::toString);
    } catch (HaltException ignored) {
      return new Result(
          1,
          true,
          () -> {
            throw new RuntimeException();
          },
          this::toString);
    }
  }

  @Override
  public String toString() {
    Argument argument = getArgument();
    if (argument == null) {
      return getClass().getSimpleName().toUpperCase();
    }
    return getClass().getSimpleName().toUpperCase() + " " + argument;
  }

  public Argument getArgument() {
    return null;
  }

  public interface Argument {

    UInt8[] bytes();
  }

  public record Result(int cycles, boolean halt, Supplier<UInt8[]> bytes, Supplier<String> text) {}
}
