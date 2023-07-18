package com.zacharyhirsch.moldynes.emulator;

import static java.lang.Byte.toUnsignedInt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Stack {

  private static final short STACK_BASE = 0x0100;
  private static final int STACK_SIZE = 0x100;

  private final ByteBuffer stack;
  private final Registers regs;

  public Stack(ByteBuffer ram, Registers regs) {
    this.stack = ram.slice(STACK_BASE, STACK_SIZE).order(ByteOrder.LITTLE_ENDIAN);
    this.regs = regs;
  }

  public void push(byte value) {
    stack.put(toUnsignedInt(regs.sp), value);
    regs.sp = (byte) (regs.sp - 1);
  }

  public void pushShort(short value) {
    stack.putShort(toUnsignedInt(regs.sp) - 1, value);
    regs.sp = (byte) (regs.sp - 2);
  }

  public byte pull() {
    regs.sp = (byte) (regs.sp + 1);
    return stack.get(toUnsignedInt(regs.sp));
  }

  public short pullShort() {
    regs.sp = (byte) (regs.sp + 2);
    return stack.getShort(toUnsignedInt(regs.sp) - 1);
  }
}
