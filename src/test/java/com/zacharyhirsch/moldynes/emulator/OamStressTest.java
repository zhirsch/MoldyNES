package com.zacharyhirsch.moldynes.emulator;

import org.junit.jupiter.api.Test;

class OamStressTest {

  @Test
  void test() {
    TestUtils.run("oam_stress.nes");
  }
}
