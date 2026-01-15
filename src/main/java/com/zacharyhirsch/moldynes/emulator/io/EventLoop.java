package com.zacharyhirsch.moldynes.emulator.io;

public interface EventLoop {

  void run(Runnable tick);
}
