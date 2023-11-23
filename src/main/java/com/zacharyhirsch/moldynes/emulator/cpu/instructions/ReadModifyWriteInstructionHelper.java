package com.zacharyhirsch.moldynes.emulator.cpu.instructions;

import com.zacharyhirsch.moldynes.emulator.cpu.NesAlu;
import com.zacharyhirsch.moldynes.emulator.NesCpuCycleContext;
import com.zacharyhirsch.moldynes.emulator.UInt16;
import com.zacharyhirsch.moldynes.emulator.UInt8;
import java.util.function.BiFunction;

final class ReadModifyWriteInstructionHelper {

//  private final String name;
//  private final UInt8 opcode;
//  private final BiFunction<NesCpuCycleContext, UInt8, UInt8> impl;
//
//  ReadModifyWriteInstructionHelper(
//      String name, UInt8 opcode, BiFunction<NesCpuCycleContext, UInt8, UInt8> impl) {
//    this.name = name;
//    this.opcode = opcode;
//    this.impl = impl;
//  }
//
//  public Instruction.Result executeZeropage(NesCpuCycleContext context) {
//    // Cycle 2
//    UInt8 adl = context.fetch(context.registers().pc.getAddressAndIncrement());
//
//    // Cycle 3
//    UInt8 ignored = context.fetch(new UInt16(UInt8.cast(0x00), adl));
//
//    // Cycle 4
//    UInt8 data = context.fetch(new UInt16(UInt8.cast(0x00), adl));
//
//    // Cycle 5
//    UInt8 result = impl.apply(context, data);
//    context.store(new UInt16(UInt8.cast(0x00), adl), result);
//
//    return new Instruction.Result(
//        () -> new UInt8[] {opcode, adl}, () -> String.format("%s $%s = %s", name, adl, data));
//  }
//
//  public Instruction.Result executeZeropageX(NesCpuCycleContext context) {
//    // Cycle 2
//    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());
//
//    // Cycle 3
//    UInt8 adl = NesAlu.add(bal, context.registers().x).output();
//    UInt8 ignored1 = context.fetch(new UInt16(UInt8.cast(0x00), bal));
//
//    // Cycle 4
//    UInt8 data = context.fetch(new UInt16(UInt8.cast(0x00), adl));
//
//    // Cycle 5
//    UInt8 result = impl.apply(context, data);
//    UInt8 ignored2 = context.fetch(new UInt16(UInt8.cast(0x00), adl));
//
//    // Cycle 6
//    context.store(new UInt16(UInt8.cast(0x00), adl), result);
//
//    return new Instruction.Result(
//        () -> new UInt8[] {opcode, bal},
//        () -> String.format("%s $%s,X @ %s = %s", name, bal, adl, data));
//  }
//
//  public Instruction.Result executeAbsoluteX(NesCpuCycleContext context) {
//    return executeAbsoluteIndexed(context, context.registers().x, "X");
//  }
//
//  public Instruction.Result executeAbsoluteY(NesCpuCycleContext context) {
//    return executeAbsoluteIndexed(context, context.registers().y, "Y");
//  }
//
//  private Instruction.Result executeAbsoluteIndexed(NesCpuCycleContext context, UInt8 index, String indexName) {
//    // Cycle 2
//    UInt8 bal = context.fetch(context.registers().pc.getAddressAndIncrement());
//
//    // Cycle 3
//    NesAlu.Result offset = NesAlu.add(bal, index);
//    UInt8 adl = offset.output();
//    UInt8 bah = context.fetch(context.registers().pc.getAddressAndIncrement());
//
//    // Cycle 4
//    UInt8 adh = NesAlu.add(bah, UInt8.cast(0), offset.c()).output();
//    UInt8 ignored1 = context.fetch(new UInt16(bah, adl));
//
//    // Cycle 5
//    UInt8 data = context.fetch(new UInt16(adh, adl));
//
//    // Cycle 6
//    UInt8 result = impl.apply(context, data);
//    UInt8 ignored2 = context.fetch(new UInt16(adh, adl));
//
//    // Cycle 7
//    context.store(new UInt16(adh, adl), result);
//
//    return new Instruction.Result(
//        () -> new UInt8[] {opcode, bal, bah},
//        () ->
//            String.format(
//                "%s $%s,%s @ %s = %s",
//                name, new UInt16(bah, bal), indexName, new UInt16(adh, adl), data));
//  }
//
//  public Instruction.Result executeIndirectY(NesCpuCycleContext context) {
//    // Cycle 2
//    UInt8 ial = context.fetch(context.registers().pc.getAddressAndIncrement());
//
//    // Cycle 3
//    UInt8 ial1 = NesAlu.add(ial, UInt8.cast(1)).output();
//    UInt8 bal = context.fetch(new UInt16(UInt8.cast(0x00), ial));
//
//    // Cycle 4
//    NesAlu.Result offset = NesAlu.add(bal, context.registers().y);
//    UInt8 adl = offset.output();
//    boolean pageCrossed = offset.c();
//    UInt8 bah = context.fetch(new UInt16(UInt8.cast(0x00), ial1));
//
//    UInt8 adh;
//    if (pageCrossed) {
//      // Cycle 5
//      adh = NesAlu.add(bah, UInt8.cast(1)).output();
//      UInt8 ignored1 = context.fetch(new UInt16(bah, adl));
//    } else {
//      adh = bah;
//    }
//
//    // Cycle 5 or 6
//    UInt8 data = context.fetch(new UInt16(adh, adl));
//
//    // Cycle 6 or 7
//    UInt8 result = impl.apply(context, data);
//    UInt8 ignored2 = context.fetch(new UInt16(adh, adl));
//
//    // Cycle 7 or 8
//    context.store(new UInt16(adh, adl), result);
//
//    return new Instruction.Result(
//        () -> new UInt8[] {opcode, ial},
//        () ->
//            String.format(
//                "%s ($%s),Y = %s @ %s = %s",
//                name, ial, new UInt16(bah, bal), new UInt16(adh, adl), data));
//  }
}
