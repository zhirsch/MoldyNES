package com.zacharyhirsch.moldynes.emulator;


import java.io.IOException;
import org.junit.jupiter.api.Test;

class NesTest {

  @Test
  void nestest() throws IOException {
    TestUtils.run("nestest.nes");
  }
}
