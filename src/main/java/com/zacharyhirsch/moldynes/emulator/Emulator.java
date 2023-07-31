package com.zacharyhirsch.moldynes.emulator;

import com.google.common.collect.Streams;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder;
import com.zacharyhirsch.moldynes.emulator.instructions.Decoder.Decoded;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Argument;
import com.zacharyhirsch.moldynes.emulator.instructions.Instruction.Result;
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
    this.regs = new Registers(pc, new StatusRegister());
    this.memory = memory;
    this.stack = new NesCpuStack(memory, regs);
  }

  public void run() {
    Decoder decoder = new Decoder(memory, regs);
    int cycle = 7; // TODO: why 7
    while (true) {
      Registers pre = regs.copy();
      Decoded decoded = decoder.next();
      Instruction.Result result = execute(decoded.instruction());
      log(cycle, pre, decoded, result);
      cycle += result.cycles();
      if (result.halt()) {
        break;
      }
      if (regs.pc.address().equals(decoded.pc())) {
        throw new InfiniteLoopException(regs);
      }
    }
  }

  private Result execute(Instruction instruction) {
    try {
      return instruction.execute2(memory, stack, regs);
    } catch (Exception exc) {
      throw new EmulatorCrashedException(regs, exc);
    }
  }

  private void log(int cycle, Registers pre, Decoded decoded, Instruction.Result result) {
    if (!DEBUG) {
      return;
    }
    Instruction instruction = decoded.instruction();
    Argument argument = instruction.getArgument();
    Stream<UInt8> bytes =
        argument == null
            ? Arrays.stream(result.bytes().get())
            : Streams.concat(Stream.of(decoded.opcode()), Arrays.stream(argument.bytes()));
    System.out.printf(
        "%s  %-8s %1s%-30s  %s PPU:  0,  0 CYC:%d\n",
        pre.pc,
        bytes.map(UInt8::toString).collect(Collectors.joining(" ")),
        instruction instanceof Undocumented ? "*" : "",
        result.text().get(),
        pre,
        cycle);
  }
}
