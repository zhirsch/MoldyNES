package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuMemory;
import com.zacharyhirsch.moldynes.emulator.NesCpuStack;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Result;

final class InstructionHelper {

  public interface Consumer4<T, U, V, W> {

    void accept(T t, U u, V v, W w);
  }

  private final String name;
  private final UInt8 opcode;
  private final Consumer4<NesCpuMemory, NesCpuStack, Registers, UInt8> impl;

  InstructionHelper(
      String name, UInt8 opcode, Consumer4<NesCpuMemory, NesCpuStack, Registers, UInt8> impl) {
    this.name = name;
    this.opcode = opcode;
    this.impl = impl;
  }

  Result executeImplied(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 ignored = memory.fetch(regs.pc.address());

    // Cycle 3
    impl.accept(memory, stack, regs, null);

    return new Result(2, false, () -> new UInt8[] {opcode}, () -> name);
  }

  Result executeImmediate(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 data = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    impl.accept(memory, stack, regs, data);

    return new Result(
        2, false, () -> new UInt8[] {opcode, data}, () -> String.format("%s #$%s", name, data));
  }

  Result executeZeropage(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 adl = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 data = memory.fetch(adl);

    // Cycle 4
    impl.accept(memory, stack, regs, data);

    return new Result(
        3,
        false,
        () -> new UInt8[] {opcode, adl},
        () -> String.format("%s $%s = %s", name, adl, data));
  }

  Result executeAbsolute(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 adl = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 adh = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 4
    UInt8 data = memory.fetch(new UInt16(adh, adl));

    // Cycle 5
    impl.accept(memory, stack, regs, data);

    return new Result(
        4,
        false,
        () -> new UInt8[] {opcode, adl, adh},
        () -> String.format("%s $%s = %s", name, new UInt16(adh, adl), data));
  }

  Result executeZeropageX(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 bal = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 ignored = memory.fetch(new UInt16(UInt8.cast(0), bal));
    UInt8 balI = NesAlu.add(bal, regs.x).output();

    // Cycle 4
    UInt8 data = memory.fetch(new UInt16(UInt8.cast(0), balI));

    // Cycle 5
    impl.accept(memory, stack, regs, data);

    return new Result(
        4,
        false,
        () -> new UInt8[] {opcode, bal},
        () -> String.format("%s $%s,X @ %s = %s", name, bal, balI, data));
  }

  Result executeZeropageY(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 bal = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 ignored = memory.fetch(new UInt16(UInt8.cast(0), bal));
    UInt8 balI = NesAlu.add(bal, regs.y).output();

    // Cycle 4
    UInt8 data = memory.fetch(new UInt16(UInt8.cast(0), balI));

    // Cycle 5
    impl.accept(memory, stack, regs, data);

    return new Result(
        4,
        false,
        () -> new UInt8[] {opcode, bal},
        () -> String.format("%s $%s,Y @ %s = %s", name, bal, balI, data));
  }

  Result executeAbsoluteX(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 bal = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 bah = memory.fetch(regs.pc.address());
    regs.pc.inc();

    NesAlu.Result balResult = NesAlu.add(bal, regs.x);
    UInt8 balI = balResult.output();

    if (balResult.c()) {
      // Cycle 4 -- Page Crossed
      UInt8 ignored = memory.fetch(new UInt16(bah, balI));
      UInt8 bah1 = NesAlu.add(bah, UInt8.cast(0), true).output();

      // Cycle 5
      UInt8 data = memory.fetch(new UInt16(bah1, balI));

      // Cycle 6
      impl.accept(memory, stack, regs, data);

      return new Result(
          5,
          false,
          () -> new UInt8[] {opcode, bal, bah},
          () ->
              String.format(
                  "%s $%s,X @ %s = %s", name, new UInt16(bah, bal), new UInt16(bah1, balI), data));
    }

    // Cycle 4 -- Same Page
    UInt8 data = memory.fetch(new UInt16(bah, balI));

    // Cycle 5
    impl.accept(memory, stack, regs, data);

    return new Result(
        4,
        false,
        () -> new UInt8[] {opcode, bal, bah},
        () ->
            String.format(
                "%s $%s,X @ %s = %s", name, new UInt16(bah, bal), new UInt16(bah, balI), data));
  }

  Result executeAbsoluteY(NesCpuMemory memory, NesCpuStack stack, Registers regs) {
    // Cycle 2
    UInt8 bal = memory.fetch(regs.pc.address());
    regs.pc.inc();

    // Cycle 3
    UInt8 bah = memory.fetch(regs.pc.address());
    regs.pc.inc();

    NesAlu.Result balResult = NesAlu.add(bal, regs.y);
    UInt8 balI = balResult.output();

    if (balResult.c()) {
      // Cycle 4 -- Page Crossed
      UInt8 ignored = memory.fetch(new UInt16(bah, balI));
      UInt8 bah1 = NesAlu.add(bah, UInt8.cast(0), true).output();

      // Cycle 5
      UInt8 data = memory.fetch(new UInt16(bah1, balI));

      // Cycle 6
      impl.accept(memory, stack, regs, data);

      return new Result(
          5,
          false,
          () -> new UInt8[] {opcode, bal, bah},
          () ->
              String.format(
                  "%s $%s,Y @ %s = %s", name, new UInt16(bah, bal), new UInt16(bah1, balI), data));
    }

    // Cycle 4 -- Same Page
    UInt8 data = memory.fetch(new UInt16(bah, balI));

    // Cycle 5
    impl.accept(memory, stack, regs, data);

    return new Result(
        4,
        false,
        () -> new UInt8[] {opcode, bal, bah},
        () ->
            String.format(
                "%s $%s,Y @ %s = %s", name, new UInt16(bah, bal), new UInt16(bah, balI), data));
  }

}
