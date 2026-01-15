package com.zacharyhirsch.moldynes.emulator.io;

public interface Audio extends AutoCloseable {

  void writeAudioSample(float sample);

  void present();
}
