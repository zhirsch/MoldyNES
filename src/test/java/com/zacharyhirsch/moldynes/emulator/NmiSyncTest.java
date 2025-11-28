package com.zacharyhirsch.moldynes.emulator;

import org.junit.jupiter.api.Test;

class NmiSyncTest {

  @Test
  void test() {
    TestUtils.run("nmi_sync/demo_ntsc.nes");
  }
}
