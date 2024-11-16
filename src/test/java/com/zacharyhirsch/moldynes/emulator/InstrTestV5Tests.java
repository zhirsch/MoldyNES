package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.io.Resources;
import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
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

  private static final class FakeDisplay implements Display {

    @Override
    public void draw(byte[] frame) {}
  }

  private void runTest(String path) throws IOException {
    ByteBuffer buffer = read("instr_test-v5/" + path);
    NesMapper mapper = NesMapper.get(buffer);
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    NesApu apu = new NesApu();
    NesPpu ppu = new NesPpu(mapper, new FakeDisplay(), loadPalette());
    NesBus bus = new NesBus(mapper, ppu, joypad1, joypad2);
    NesCpu cpu = new NesCpu(ppu, bus);
    Emulator emulator = new Emulator(cpu, ppu, apu);

    waitForStart(emulator, bus);
    byte status = (byte) 0xff;
    while (emulator.step()) {
      status = readByte(bus, (short) 0x6000);
      if (status == (byte) 0x81) {
        reset(emulator, bus);
      }
      if (status >= 0) {
        break;
      }
    }

    System.out.println(readStatusString(bus));

    assertThat(status).isEqualTo(0x00);
  }

  private static NesPpuPalette loadPalette() throws IOException {
    try (InputStream input = Resources.getResource("Composite_wiki.pal").openStream()) {
      return NesPpuPalette.load(input);
    }
  }

  @Test
  void basics() throws Exception {
    runTest("rom_singles/01-basics.nes");
  }

  @Test
  void implied() throws Exception {
    runTest("rom_singles/02-implied.nes");
  }

  @Test
  void immediate() throws Exception {
    runTest("rom_singles/03-immediate.nes");
  }

  @Test
  void zero_page() throws Exception {
    runTest("rom_singles/04-zero_page.nes");
  }

  @Test
  void zp_xy() throws Exception {
    runTest("rom_singles/05-zp_xy.nes");
  }

  @Test
  void absolute() throws Exception {
    runTest("rom_singles/06-absolute.nes");
  }

  @Test
  void abs_xy() throws Exception {
    runTest("rom_singles/07-abs_xy.nes");
  }

  @Test
  void ind_x() throws Exception {
    runTest("rom_singles/08-ind_x.nes");
  }

  @Test
  void ind_y() throws Exception {
    runTest("rom_singles/09-ind_y.nes");
  }

  @Test
  void branches() throws Exception {
    runTest("rom_singles/10-branches.nes");
  }

  @Test
  void stack() throws Exception {
    runTest("rom_singles/11-stack.nes");
  }

  @Test
  void jmp_jsr() throws Exception {
    runTest("rom_singles/12-jmp_jsr.nes");
  }

  @Test
  void rts() throws Exception {
    runTest("rom_singles/13-rts.nes");
  }

  @Test
  void rti() throws Exception {
    runTest("rom_singles/14-rti.nes");
  }

  @Test
  void brk() throws Exception {
    runTest("rom_singles/15-brk.nes");
  }

  @Test
  void special() throws Exception {
    runTest("rom_singles/16-special.nes");
  }

  private static byte readByte(NesBus bus, short address) {
    return bus.read((byte) (address >>> 8), (byte) address);
  }

  private static String readStatusString(NesBus bus) {
    short address = 0x6004;
    StringBuilder accum = new StringBuilder();
    for (byte ch = readByte(bus, address++); ch != 0; ch = readByte(bus, address++)) {
      accum.append((char) ch);
    }
    return accum.toString();
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
      byte status1 = readByte(bus, (short) 0x6000);
      byte status2 = readByte(bus, (short) 0x6001);
      byte status3 = readByte(bus, (short) 0x6002);
      byte status4 = readByte(bus, (short) 0x6003);
      if (status1 == (byte) 0x80
          && status2 == (byte) 0xde
          && status3 == (byte) 0xb0
          && status4 == (byte) 0x61) {
        break;
      }
    }
  }
}
