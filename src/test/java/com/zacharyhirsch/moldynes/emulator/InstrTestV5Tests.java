package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class InstrTestV5Tests {

  private ByteBuffer read(String path) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }
      return ByteBuffer.wrap(is.readAllBytes());
    }
  }

  private void runTest(String filename) throws IOException {
    ByteBuffer buffer = read("instr_test-v5\\rom_singles\\" + filename);
    NesMapper mapper = NesMapper.get(buffer);
    NesApu apu = new NesApu();
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    try (Display display = new Display(joypad1, joypad2)) {
      NesPpu ppu = new NesPpu(mapper, display);
      NesBus bus = new NesBus(mapper, ppu, joypad1, joypad2);
      NesCpu cpu = new NesCpu(ppu, bus);

      byte status = run(new Emulator(cpu, ppu, apu), bus, display);

      assertThat(status).isEqualTo(0x00);
    }
  }

  @Test
  void basics() throws Exception {
    runTest("01-basics.nes");
  }

  @Test
  void implied() throws Exception {
    runTest("02-implied.nes");
  }

  @Test
  void immediate() throws Exception {
    runTest("03-immediate.nes");
  }

  @Test
  void zero_page() throws Exception {
    runTest("04-zero_page.nes");
  }

  @Test
  void zp_xy() throws Exception {
    runTest("05-zp_xy.nes");
  }

  @Test
  void absolute() throws Exception {
    runTest("06-absolute.nes");
  }

  @Test
  void abs_xy() throws Exception {
    runTest("07-abs_xy.nes");
  }

  @Test
  void ind_x() throws Exception {
    runTest("08-ind_x.nes");
  }

  @Test
  void ind_y() throws Exception {
    runTest("09-ind_y.nes");
  }

  @Test
  void branches() throws Exception {
    runTest("10-branches.nes");
  }

  @Test
  void stack() throws Exception {
    runTest("11-stack.nes");
  }

  @Test
  void jmp_jsr() throws Exception {
    runTest("12-jmp_jsr.nes");
  }

  @Test
  void rts() throws Exception {
    runTest("13-rts.nes");
  }

  @Test
  void rti() throws Exception {
    runTest("14-rti.nes");
  }

  @Test
  void brk() throws Exception {
    runTest("15-brk.nes");
  }

  @Test
  void special() throws Exception {
    runTest("16-special.nes");
  }

  private static byte run(Emulator emulator, NesBus bus, Display display) {
    waitForStart(emulator, bus);
    byte status = (byte) 0xff;
    while (emulator.step()) {
      status = bus.read((byte) 0x60, (byte) 0x00);
      if (status == (byte) 0x81) {
        reset(emulator, bus);
      }
      if (display.quit) {
        break;
      }
    }
    return status;
  }

  private static void reset(Emulator emulator, NesBus bus) {
    Instant resetAt = Instant.now().plusMillis(250);
    while (emulator.step()) {
      if (Instant.now().isAfter(resetAt)) {
        emulator.reset();
        break;
      }
    }
    waitForStart(emulator, bus);
  }

  private static void waitForStart(Emulator emulator, NesBus bus) {
    while (emulator.step()) {
      byte status = bus.read((byte) 0x60, (byte) 0x00);
      if (status == (byte) 0x80) {
        break;
      }
    }
  }
}
