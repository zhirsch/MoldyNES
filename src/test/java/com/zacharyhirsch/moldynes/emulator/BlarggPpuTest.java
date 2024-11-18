package com.zacharyhirsch.moldynes.emulator;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class BlarggPpuTest {

  @Test
  void pputest() throws IOException {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/vbl_clear_time.nes");
  }
}
