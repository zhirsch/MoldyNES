package com.zacharyhirsch.moldynes.emulator;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class VblNmiTimingTest {

  @Test
  void test1() throws IOException {
    TestUtils.run("vbl_nmi_timing/1.frame_basics.nes");
  }
  @Test
  void test2() throws IOException {
    TestUtils.run("vbl_nmi_timing/2.vbl_timing.nes");
  }
  @Test
  void test3() throws IOException {
    TestUtils.run("vbl_nmi_timing/3.even_odd_frames.nes");
  }
  @Test
  void test4() throws IOException {
    TestUtils.run("vbl_nmi_timing/4.vbl_clear_timing.nes");
  }
  @Test
  void test5() throws IOException {
    TestUtils.run("vbl_nmi_timing/5.nmi_suppression.nes");
  }
  @Test
  void test6() throws IOException {
    TestUtils.run("vbl_nmi_timing/6.nmi_disable.nes");
  }
  @Test
  void test7() throws IOException {
    TestUtils.run("vbl_nmi_timing/7.nmi_timing.nes");
  }
}
