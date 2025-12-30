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

  double mix() {
    return Math.clamp(mixPulseChannels() + mixTndChannels(), 0.0, 1.0);
  }

  private double mixPulseChannels() {
    int p1 = pulse1.getCurrentVolume();
    int p2 = pulse2.getCurrentVolume();
    if (p1 + p2 == 0) {
      return 0;
    }
    return 95.88 / (100 + (8128. / (p1 + p2)));
  }

  private double mixTndChannels() {
    int t = triangle.getCurrentVolume();
    int n = noise.getCurrentVolume();
    int d = dmc.getCurrentVolume();
    if (t + n + d == 0) {
      return 0;
    }
    return 159.79 / (100 + 1 / ((t / 8227.) + (n / 12241.) + (d / 22638.)));
  }
}
