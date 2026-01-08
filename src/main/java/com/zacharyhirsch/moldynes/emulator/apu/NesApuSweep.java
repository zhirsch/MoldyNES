package com.zacharyhirsch.moldynes.emulator.apu;

final class NesApuSweep {

  private final NesApuPulse pulse;

  private boolean enabled;
  private int period;
  private boolean negate;
  private int shift;

  private boolean reload;
  private int value;

  NesApuSweep(NesApuPulse pulse) {
    this.pulse = pulse;
    this.enabled = false;
    this.period = 0;
    this.negate = false;
    this.shift = 0;
    this.reload = false;
    this.value = 0;
  }

  void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  void setPeriod(int period) {
    this.period = period;
  }

  void setNegate(boolean negate) {
    this.negate = negate;
  }

  void setShift(int shift) {
    this.shift = shift;
  }

  void tick() {
    if (value == 0 && enabled && shift > 0) {
      if (!isMuting()) {
        pulse.timer().setPeriod(getTargetPeriod());
      }
    }
    if (value == 0 || reload) {
      value = period;
      reload = false;
    } else {
      value--;
    }
  }

  boolean isMuting() {
    return getTargetPeriod() > 0x7ff;
  }

  private int getTargetPeriod() {
    return Math.clamp(pulse.timer().getPeriod() + getDelta(), 0, Integer.MAX_VALUE);
  }

  private int getDelta() {
    int delta = pulse.timer().getPeriod() >>> shift;
    if (negate) {
      return switch (pulse.getIndex()) {
        case 1 -> ~delta;
        case 2 -> -delta;
        default -> throw new IllegalStateException("pulse%d".formatted(pulse.getIndex()));
      };
    }
    return delta;
  }
}
