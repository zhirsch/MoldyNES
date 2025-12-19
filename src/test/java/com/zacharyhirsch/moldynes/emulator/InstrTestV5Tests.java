package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.zacharyhirsch.moldynes.emulator.io.Display;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpuPalette;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import com.zacharyhirsch.moldynes.emulator.rom.NesRomLoader;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class InstrTestV5Tests {

  private static final class FakeDisplay implements Display {

    @Override
    public void draw(byte[] frame) {}
  }

  private static void runTest(String path) {
    NesRom rom = NesRomLoader.load("roms/nes-test-roms/instr_test-v5/%s".formatted(path));
    NesBus bus =
        new NesBus(
            NesMapper.load(rom),
            NesPpuPalette.load("Composite_wiki.pal"),
            new FakeDisplay(),
            new NesJoypad(),
            new NesJoypad());

    waitForStart(bus);
    byte status = (byte) 0xff;
    while (bus.isRunning()) {
      bus.tick();
      status = readByte(bus, (short) 0x6000);
      if (status == (byte) 0x81) {
        reset(bus);
      }
      if (status >= 0) {
        break;
      }
    }

    System.out.println(readStatusString(bus));

    assertThat(status).isEqualTo(0x00);
  }

  @Test
  void basics() {
    runTest("rom_singles/01-basics.nes");
  }

  @Test
  void implied() {
    runTest("rom_singles/02-implied.nes");
  }

  @Test
  void immediate() {
    runTest("rom_singles/03-immediate.nes");
  }

  @Test
  void zero_page() {
    runTest("rom_singles/04-zero_page.nes");
  }

  @Test
  void zp_xy() {
    runTest("rom_singles/05-zp_xy.nes");
  }

  @Test
  void absolute() {
    runTest("rom_singles/06-absolute.nes");
  }

  @Test
  void abs_xy() {
    runTest("rom_singles/07-abs_xy.nes");
  }

  @Test
  void ind_x() {
    runTest("rom_singles/08-ind_x.nes");
  }

  @Test
  void ind_y() {
    runTest("rom_singles/09-ind_y.nes");
  }

  @Test
  void branches() {
    runTest("rom_singles/10-branches.nes");
  }

  @Test
  void stack() {
    runTest("rom_singles/11-stack.nes");
  }

  @Test
  void jmp_jsr() {
    runTest("rom_singles/12-jmp_jsr.nes");
  }

  @Test
  void rts() {
    runTest("rom_singles/13-rts.nes");
  }

  @Test
  void rti() {
    runTest("rom_singles/14-rti.nes");
  }

  @Test
  void brk() {
    runTest("rom_singles/15-brk.nes");
  }

  @Test
  void special() {
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

  private static void reset(NesBus bus) {
    Instant resetAt = Instant.now().plusMillis(250);
    while (true) {
      bus.tick();
      if (Instant.now().isAfter(resetAt)) {
        bus.reset();
        break;
      }
    }
    waitForStart(bus);
  }

  private static void waitForStart(NesBus bus) {
    while (true) {
      bus.tick();
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
