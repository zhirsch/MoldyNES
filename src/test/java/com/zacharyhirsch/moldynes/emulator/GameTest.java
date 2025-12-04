package com.zacharyhirsch.moldynes.emulator;

import org.junit.jupiter.api.Test;

class GameTest {

  @Test
  void pacman() {
    TestUtils.run("pacman.nes");
  }

  @Test
  void donkeykong() {
    TestUtils.run("donkeykong.nes");
  }

  @Test
  void smb() {
    TestUtils.run("smb.nes");
  }

  @Test
  void iceclimber() {
    TestUtils.run("iceclimber.nes");
  }
}
