package com.zacharyhirsch.moldynes.emulator;

public class InfiniteLoopException extends RuntimeException {

  public InfiniteLoopException(int pc) {
    super(String.format("infinite loop detected at %04x", pc));
  }
}
