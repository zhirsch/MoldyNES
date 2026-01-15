package com.zacharyhirsch.moldynes.emulator.io;

public interface Io extends AutoCloseable {

  Audio audio();

  Video video();

  Joypads joypads();

  EventLoop eventLoop();
}
