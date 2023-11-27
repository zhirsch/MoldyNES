package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.logging.NesCpuLogger;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemoryMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    try (Display display = new Display()) {
      ByteBuffer buffer = read("instr_test-v5\\rom_singles\\" + filename);
      NesMemoryMapper mapper = NesMemoryMapper.get(buffer);
      NesPpu ppu = mapper.createPpu(buffer, display);
      NesApu apu = new NesApu();
      NesMemory memory = mapper.createMem(buffer, ppu, apu);

      NesCpu cpu = new NesCpu(ppu, memory, new NesCpuLogger(OutputStream.nullOutputStream()));
      byte status = run(new Emulator(cpu, ppu, apu), memory, display);

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

  private static byte run(Emulator emulator, NesMemory memory, Display display) {
    waitForStart(emulator, memory);
    byte finalStatus = (byte) 0xff;
    Byte lastStatus = null;
    String lastMessage = null;
    while (emulator.step()) {
      byte status = memory.fetch((byte) 0x60, (byte) 0x00);
      if (lastStatus == null || lastStatus != status) {
        lastStatus = status;
        System.out.printf("STATUS: %02x\n", status);
      }
      if (Byte.toUnsignedInt(status) < 0x80) {
        finalStatus = status;
      }
      if (status == (byte) 0x81) {
        reset(emulator, memory);
      }
      StringBuilder builder = new StringBuilder();
      for (short addr = 0x6004; ; addr += 1) {
        byte ch = memory.fetch((byte) (addr >>> 8), (byte) (addr & 0xff));
        if (ch == 0) {
          break;
        }
        builder.append((char) ch);
      }
      String message = builder.toString();
      if (lastMessage == null || !lastMessage.equals(message)) {
        lastMessage = message;
        System.out.printf(">%s<\n", message);
      }
      if (display.quit) {
        break;
      }
    }
    return finalStatus;
  }

  private static void reset(Emulator emulator, NesMemory memory) {
    Instant resetAt = Instant.now().plusMillis(250);
    while (emulator.step()) {
      if (Instant.now().isAfter(resetAt)) {
        emulator.reset();
        break;
      }
    }
    waitForStart(emulator, memory);
  }

  private static void waitForStart(Emulator emulator, NesMemory memory) {
    while (emulator.step()) {
      byte status = memory.fetch((byte) 0x60, (byte) 0x00);
      if (status == (byte) 0x80) {
        break;
      }
    }
  }
}
