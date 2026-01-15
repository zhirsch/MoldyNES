package com.zacharyhirsch.moldynes.emulator.io;

public interface Io extends AutoCloseable {

  Audio audio();

  Video video();

  Joypad joypad1();

  Joypad joypad2();

  EventLoop eventLoop();
}
