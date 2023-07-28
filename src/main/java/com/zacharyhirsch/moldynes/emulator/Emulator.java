package com.zacharyhirsch.moldynes.emulator;

import com.google.common.collect.Streams;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder.Decoded;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import com.zacharyhirsch.moldynes.emulator.instructions.Undocumented;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Emulator {

  private static final boolean DEBUG = true;

  private final Registers regs;
  private final NesCpuMemory memory;
  private final NesCpuStack stack;

  public Emulator(NesCpuMemory memory, ProgramCounter pc) {
    this.regs = new Registers(pc);
    this.memory = memory;
    this.stack = new NesCpuStack(memory, regs);
  }

  public void run() {
    Decoder decoder = new Decoder(memory, regs);
    for (int cycle = 1; cycle < 10_000; cycle++) {
      Decoded decoded = decoder.next();
      if (DEBUG) {
        System.out.printf(
            "%s  %-8s %1s%-30s  %s PPU:  0,  0 CYC:%d\n",
            decoded.pc(),
            disassemble(decoded.opcode(), decoded.instruction()),
            decoded.instruction() instanceof Undocumented ? "*" : "",
            decoded.instruction(),
            regs,
            cycle);
      }
      if (!execute(decoded.instruction())) {
        break;
      }
      if (regs.pc.address().equals(decoded.pc())) {
        throw new InfiniteLoopException(regs);
      }
    }
  }

  private boolean execute(Instruction instruction) {
    try {
      instruction.execute(memory, stack, regs);
      return true;
    } catch (HaltException ignored) {
      return false;
    } catch (Exception exc) {
      throw new EmulatorCrashedException(regs, exc);
    }
  }

  private String disassemble(UInt8 opcode, Instruction instruction) {
    return Streams.concat(Stream.of(opcode), Arrays.stream(instruction.getArgument().getBytes()))
        .map(UInt8::toString)
        .collect(Collectors.joining(" "));
  }
}
