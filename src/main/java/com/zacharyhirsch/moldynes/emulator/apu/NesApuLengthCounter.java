package com.zacharyhirsch.moldynes.emulator.apu;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class NesApuLengthCounter {

  private static final Logger log = LoggerFactory.getLogger(NesApuLengthCounter.class);

  private static final Map<Integer, Integer> LENGTH_TABLE =
      Map.ofEntries(
          Map.entry(0x00, 10),
          Map.entry(0x01, 254),
          Map.entry(0x02, 20),
          Map.entry(0x03, 2),
          Map.entry(0x04, 40),
          Map.entry(0x05, 4),
          Map.entry(0x06, 80),
          Map.entry(0x07, 6),
          Map.entry(0x08, 160),
          Map.entry(0x09, 8),
          Map.entry(0x0a, 60),
          Map.entry(0x0b, 10),
          Map.entry(0x0c, 14),
          Map.entry(0x0d, 12),
          Map.entry(0x0e, 26),
          Map.entry(0x0f, 14),
          Map.entry(0x10, 12),
          Map.entry(0x11, 16),
          Map.entry(0x12, 24),
          Map.entry(0x13, 18),
          Map.entry(0x14, 48),
          Map.entry(0x15, 20),
          Map.entry(0x16, 96),
          Map.entry(0x17, 22),
          Map.entry(0x18, 192),
          Map.entry(0x19, 24),
          Map.entry(0x1a, 72),
          Map.entry(0x1b, 26),
          Map.entry(0x1c, 16),
          Map.entry(0x1d, 28),
          Map.entry(0x1e, 32),
          Map.entry(0x1f, 30));

  private int value;
  private boolean halted;
  private boolean suppressNextReset;

  NesApuLengthCounter() {
    this.value = 0;
    this.halted = false;
    this.suppressNextReset = false;
  }

  void tick() {
    if (value == 0) {
      return;
    }
    if (halted) {
      return;
    }
    value--;
  }

  int value() {
    return value;
  }

  void clear() {
    log.info("APU length counter cleared");
    value = 0;
  }

  void suppressNextReset() {
    suppressNextReset = true;
  }

  void reset(int value) {
    if (!suppressNextReset) {
      assert 0x00 <= value && value <= 0x1f;
      this.value = LENGTH_TABLE.get(value);
      log.info("APU length counter set to {}", this.value);
    }
    suppressNextReset = false;
  }

  void setHalted(boolean halted) {
    this.halted = halted;
  }
}
