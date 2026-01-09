package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuFilter {

  private final FilterStage hpf90;
  private final FilterStage hpf440;
  private final FilterStage lpf14k;

  NesApuFilter(double sampleRate) {
    // NES hardware: two high-pass and one low-pass filter
    this.hpf90 = new FilterStage(FilterType.HIGH_PASS, 90.0, sampleRate);
    this.hpf440 = new FilterStage(FilterType.HIGH_PASS, 440.0, sampleRate);
    this.lpf14k = new FilterStage(FilterType.LOW_PASS, 14000.0, sampleRate);
  }

  double process(double input) {
    // Chain the filters in series
    double out = hpf90.process(input);
    out = hpf440.process(out);
    out = lpf14k.process(out);
    return out;
  }

  private enum FilterType {
    HIGH_PASS,
    LOW_PASS
  }

  private static final class FilterStage {

    private final double alpha;
    private final FilterType type;

    private double prevInput = 0.0;
    private double prevOutput = 0.0;

    public FilterStage(FilterType type, double cutoff, double sampleRate) {
      this.type = type;
      double dt = 1.0 / sampleRate;
      double rc = 1.0 / (2 * Math.PI * cutoff);

      if (type == FilterType.HIGH_PASS) {
        this.alpha = rc / (rc + dt);
      } else { // LOW_PASS
        this.alpha = dt / (rc + dt);
      }
    }

    public double process(double input) {
      double output;
      if (type == FilterType.HIGH_PASS) {
        output = alpha * (prevOutput + input - prevInput);
      } else {
        output = prevOutput + alpha * (input - prevOutput);
      }
      prevInput = input;
      prevOutput = output;
      return output;
    }
  }
}
