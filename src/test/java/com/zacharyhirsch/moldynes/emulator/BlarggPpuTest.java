package com.zacharyhirsch.moldynes.emulator;

import org.junit.jupiter.api.Test;

class BlarggPpuTest {

  @Test
  void palette_ram() {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/palette_ram.nes");
  }

  @Test
  void power_up_palette() {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/power_up_palette.nes");
  }

  @Test
  void sprite_ram() {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/sprite_ram.nes");
  }

  @Test
  void vbl_clear_time() {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/vbl_clear_time.nes");
  }

  @Test
  void vram_access() {
    TestUtils.run("blargg_ppu_tests_2005.09.15b/vram_access.nes");
  }
}
