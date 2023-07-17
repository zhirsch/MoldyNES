package com.zacharyhirsch.moldynes.emulator.instructions;

import java.nio.ByteBuffer;

public interface Instruction {

  static Instruction decode(ByteBuffer ram) {
    byte opcode = ram.get();
    return switch (opcode) {
      case (byte) 0x4c -> new JmpAbsolute(ram.getShort());
      case (byte) 0x8d -> new StaAbsolute(ram.getShort());
      case (byte) 0x88 -> new Dey();
      case (byte) 0x9a -> new Txs();
      case (byte) 0xa0 -> new LdyImmediate(ram.get());
      case (byte) 0xa2 -> new LdxImmediate(ram.get());
      case (byte) 0xa9 -> new LdaImmediate(ram.get());
      case (byte) 0xca -> new Dex();
      case (byte) 0xd0 -> new BneRelative(ram.get());
      case (byte) 0xd8 -> new Cld();
      case (byte) 0xf0 -> new BeqRelative(ram.get());
      default -> throw new UnknownOpcodeException(opcode);
    };
  }

  String describe();
}
