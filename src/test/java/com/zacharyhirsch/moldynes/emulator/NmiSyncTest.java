package com.zacharyhirsch.moldynes.emulator;


import java.io.IOException;
import org.junit.jupiter.api.Test;

class NmiSyncTest {

  @Test
  void pputest() throws IOException {
    TestUtils.run("nmi_sync/demo_ntsc.nes");
  }
}
