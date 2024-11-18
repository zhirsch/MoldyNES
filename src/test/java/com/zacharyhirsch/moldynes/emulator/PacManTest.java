package com.zacharyhirsch.moldynes.emulator;


import java.io.IOException;
import org.junit.jupiter.api.Test;

class PacManTest {

  @Test
  void pacman() throws IOException {
    TestUtils.run("pacman.nes");
  }
}
