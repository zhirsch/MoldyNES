package com.zacharyhirsch.moldynes.emulator.apu;

// https://www.nesdev.org/wiki/APU_Mixer
final class NesApuMixer {

  private final NesApuPulse pulse1;
  private final NesApuPulse pulse2;
  private final NesApuTriangle triangle;
  private final NesApuNoise noise;
  private final NesApuDmc dmc;

  NesApuMixer(
      NesApuPulse pulse1,
      NesApuPulse pulse2,
      NesApuTriangle triangle,
      NesApuNoise noise,
      NesApuDmc dmc) {
    this.pulse1 = pulse1;
    this.pulse2 = pulse2;
    this.triangle = triangle;
    this.noise = noise;
    this.dmc = dmc;
  }

  float mix() {
    return Math.clamp(mixPulseChannels() + mixTndChannels(), 0.0f, 1.0f);
  }

  private float mixPulseChannels() {
    int p1 = pulse1.getCurrentVolume();
    int p2 = pulse2.getCurrentVolume();
    if (p1 + p2 == 0) {
      return 0;
    }
    return 95.88f / (100 + (8128.0f / (p1 + p2)));
  }

  private float mixTndChannels() {
    int t = triangle.getCurrentVolume();
    int n = noise.getCurrentVolume();
    int d = dmc.getCurrentVolume();
    if (t + n + d == 0) {
      return 0;
    }
    return 159.79f / (100 + 1 / ((t / 8227.0f) + (n / 12241.0f) + (d / 22638.0f)));
  }
}
